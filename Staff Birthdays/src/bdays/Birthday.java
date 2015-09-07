package bdays;

public enum Birthday {
	BAY, PIGEON, EMI;

	public static Birthday getBirthday(String name) {
		for (Birthday birthday : Birthday.values()) {
			if (birthday.toString().equalsIgnoreCase(name)) {
				return birthday;
			}
		}
		return null;
	}
}
