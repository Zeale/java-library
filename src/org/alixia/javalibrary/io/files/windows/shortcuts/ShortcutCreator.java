package org.alixia.javalibrary.io.files.windows.shortcuts;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ShortcutCreator {
	public static void createShortcut(File location, File pointTo, File icon, String description)
			throws FileNotFoundException, IOException {

		createShortcut(new FileOutputStream(location), pointTo, icon, description);

	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
//		createShortcut(System.out, new File("C:/Potato.txt"), new File("C:/"));
	}

	public static void createShortcut(OutputStream out, File pointTo, File icon, String description)
			throws FileNotFoundException, IOException {

		try (DataOutputStream o = new DataOutputStream(out)) {
			o.writeInt(0x4C);// Header Size
			o.write(new byte[] { 0, 2, 0x14, 1, 0, 0, 0, 0, (byte) 0xC0, 0, 0, 0, 0, 0, 0, 0x46 });// LinkCLSID

			// Link Flags
			byte flg1 = (byte) 0b10000001;// HasLinkTargetIDList and Shell Link contains Unicode encoded strings.
											// (IsUnicode)
			if (description != null)
				flg1 |= 0b00100000;// Add a shortcut description. (HasName)
			if (icon != null)
				flg1 |= 0b00000010;// Add an icon. (HasIconLocation)
			o.write(new byte[] { flg1, 0, 0, 0 });

			// File Attributes
			o.write(new byte[] { 0b00000010, 0, 0, 0 });// FILE_ATTRIBUTE_NORMAL

			o.writeLong(0);// CreationTime
			o.writeLong(0);// AccessTime
			o.writeLong(0);// WriteTime
			o.writeInt(0);// FileSize

			o.writeInt(0);// Icon Index
			o.write(1);// Show Command

			// HotKeyFlags
			o.writeByte(0x53);// LowByte
			o.writeByte(0b00000111);// HighByte

			// The above should result in Ctrl + Shift + Alt being the hotkey used to open
			// the application.

			// Now for reserved blocks.
			o.writeShort(0);
			o.writeLong(0);

			// This completes the SHELL_LINK_HEADER. Now we need to fill out the
			// LINKTARGET_IDLIST.

		}

	}
}
