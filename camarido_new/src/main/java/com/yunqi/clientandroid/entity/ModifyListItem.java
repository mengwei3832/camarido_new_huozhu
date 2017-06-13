package com.yunqi.clientandroid.entity;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 订单执行过程列表的字段
 * @date 15/12/17
 */
public class ModifyListItem extends IDontObfuscate {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 6287965952522768986L;

	// public String ticketOperationPicName ;//图片名称
	// public String ticketWeight ;//吨数
	// public String ticketMemo ;//备注
	// public String ticketOperationPicUrl ;//图片URL
	// public String ticketRelationCode ;//相关单号
	// public String ticketId ;//订单Id
	// public int ticketOperationPicIndex ;//图片Id
	// public String ticketOperator ;//操作人
	// public String ticketOperationType ;//执行状态
	// public String ticketSettleAmount ;//实结金额
	// public String ticketSettleMemo ;//结算备注
	// public String createTime ;//创建时间

	public int id; // 操作ID
	public int TicketId; // 订单Id
	public String TicketOperator; // 操作人
	public String TicketRelationCode; // 相关单号
	public float TicketWeight; // 吨数
	public int TicketOperationPicIndex; // 图片Id
	public String TicketOperationPicUrl; // 图片Url
	public String TicketOperationPicUrlImg800; // 图片Url(大图)
	public String TicketOperationPicName; // 图片名称
	public String TicketMemo; // 备注
	public int TicketOperationType; // 10：开始执行；15：修改提货单；20：上传提货单；30：上传装货单；40：上传签收单；50：审核；60：结算；70：禁用；80：取消；
	public double TicketSettleAmount; // 实结金额
	public String TicketSettleMemo; // 结算备注
	public String CreateTime; // 创建时间
	public int TicketOperationStatus; // 订单操作状态：0：未审核；1：审核未通过；2：已审核

}
