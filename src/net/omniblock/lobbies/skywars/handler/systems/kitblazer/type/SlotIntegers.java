package net.omniblock.lobbies.skywars.handler.systems.kitblazer.type;

public enum SlotIntegers {
	
	 Slots_1(39, 38, 37, 36),
	 Slots_2(27, 18, 9),
	 Slots_3(0, 1, 2, 3, 4, 5, 6, 7, 8),
	 Slots_4(17, 26, 35, 44),
	 Slots_5(43, 42, 41)
	 ;

	private Integer[] integers;

	SlotIntegers(Integer... integers) {
		this.setIntegers(integers);
	}

	public Integer[] getIntegers() {
		return integers;
	}

	public void setIntegers(Integer[] integers) {
		this.integers = integers;
	}
	
}
