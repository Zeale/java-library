package zeale.apps.stuff_modules.discord.bots.taige.api.bot.data;

import java.io.File;

public final class GuildData extends Data {

	private final static String WELCOME_MESSAGE_CHANNEL = "welcome-message-channel", JOIN_ROLE_LIST = "join-role-list",
			COUNT_TO_ONE_MILLION_CHANNEL = "count-to-one-million-channel", CURRENCY_NAME = "currency-name",
			PRIZE_ANNOUNCEMENTS_CHANNEL = "prize-announcements-channel",
			COUNT_TO_ONE_MILLION_CURRENT_COUNT = "count-to-one-million-current-count", MUTE_ROLE = "mute-role",
			USERS_WITH_MUTE_PERMS_LIST = "users-with-mute-perms-list";

	GuildData(File datafile, String id) {
		super(datafile, id);
	}

	public String getCountToOneMillionChannel() {
		return getData().get(COUNT_TO_ONE_MILLION_CHANNEL);
	}

	public String getCountToOneMillionCurrentCount() {
		return getData().get(COUNT_TO_ONE_MILLION_CURRENT_COUNT);
	}

	public String getCurrencyName() {
		return getData().get(CURRENCY_NAME);
	}

	public String[] getJoinRoleList() {
		return ARRAY_TO_STRING_GATEWAY.from(getData().get(JOIN_ROLE_LIST));
	}

	public String getMuteRole() {
		return getData().get(MUTE_ROLE);
	}

	public String getPrizeAnnouncementsChannel() {
		return getData().get(PRIZE_ANNOUNCEMENTS_CHANNEL);
	}

	public String[] getUsersWithMutePermsList() {
		return ARRAY_TO_STRING_GATEWAY.from(getData().get(USERS_WITH_MUTE_PERMS_LIST));
	}

	public String getWelcomeMessageChannel() {
		return getData().get(WELCOME_MESSAGE_CHANNEL);
	}

	public void setCountToOneMillionChannel(String channelID) {
		if (channelID == null)
			getData().remove(COUNT_TO_ONE_MILLION_CHANNEL);
		else
			getData().put(COUNT_TO_ONE_MILLION_CHANNEL, channelID);
	}

	public void setCountToOneMillionCurrentCount(String count) {
		if (count == null)
			getData().remove(COUNT_TO_ONE_MILLION_CURRENT_COUNT);
		else
			getData().put(COUNT_TO_ONE_MILLION_CURRENT_COUNT, count);
	}

	public void setCurrencyName(String name) {
		if (name == null)
			getData().remove(CURRENCY_NAME);
		else
			getData().put(CURRENCY_NAME, name);
	}

	public void setJoinRoleList(String... list) {
		getData().put(JOIN_ROLE_LIST, ARRAY_TO_STRING_GATEWAY.to(list));
	}

	public void setMuteRole(String roleID) {
		if (roleID == null)
			getData().remove(MUTE_ROLE);
		else
			getData().put(MUTE_ROLE, roleID);
	}

	public void setPrizeAnnouncementsChannel(String channelID) {
		if (channelID == null)
			getData().remove(channelID);
		else
			getData().put(PRIZE_ANNOUNCEMENTS_CHANNEL, channelID);
	}

	public void setUsersWithMutePermsList(String... list) {
		getData().put(USERS_WITH_MUTE_PERMS_LIST, ARRAY_TO_STRING_GATEWAY.to(list));
	}

	public void setWelcomeMessageChannel(String channelID) {
		if (channelID == null)
			getData().remove(WELCOME_MESSAGE_CHANNEL);
		else
			getData().put(WELCOME_MESSAGE_CHANNEL, channelID);
	}

}
