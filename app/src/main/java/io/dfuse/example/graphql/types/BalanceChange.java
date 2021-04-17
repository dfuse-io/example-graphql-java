package io.dfuse.example.graphql.types;

public class BalanceChange {
	public String address;
	public String oldValue;
	public String newValue;
	public Reason reason;

	public static enum Reason {
		UNKNOWN, REWARD_MINE_UNCLE, REWARD_MINE_BLOCK, REWARD_TRANSACTION_FEE, DAO_REFUND_CONTRACT, DAO_ADJUST_BALANCE,
		TRANSFER, GENESIS_BALANCE, GAS_BUY, GAS_REFUND, TOUCH_ACCOUNT, SUICIDE_WITHDRAW, SUICIDE_REFUND;
	}
}
