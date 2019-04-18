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
			o.write(new byte[] { 0, 0, 0, 0x4C });// Header Size
			o.write(new byte[] { 0, 2, 0x14, 1, 0, 0, 0, 0, (byte) 0xC0, 0, 0, 0, 0, 0, 0, 0x46 });// LinkCLSID

			// Link Flags
			byte flg1 = 1;// Shell Link contains Unicode encoded strings. (IsUnicode)
			if (description != null)
				flg1 |= 0b00100000;// Add a shortcut description. (HasName)
			if (icon != null)
				flg1 |= 0b00000010;// Add an icon. (HasIconLocation)
			o.write(new byte[] { flg1, 0, 0, 0 });

			// File Attributes
			o.write(new byte[] { 0b00000010, 0, 0, 0 });// FILE_ATTRIBUTE_NORMAL
			
			

		}

	}
}
