package net.minecraft.src;

public class IFI_Statics {

	/**
	 * サーバーへFigureのスポーン情報を送付する。
	 * 初期設置時のみ。
	 * 0 : 0x00
	 * 1-4  : x
	 * 5-8  : y
	 * 9-12 : z
	 * 13-16 : rotY
	 * 17- : EntityName
	 */
	public static final byte IFI_Server_SpawnFigure		= (byte)0x00;
	/**
	 * 0 : 0x81
	 * 1-4 : EntityID
	 * 5 : UpdateCount
	 * 6-9   : rotBaseYaw
	 * 10-13 : zoom
	 * 14 : Flags
	 * 15-18 : rotHeadYaw
	 * 19-22 : rotHeadPitch
	 */
	public static final byte IFI_Packet_Data			= (byte)0x81;
	/**
	 * 0 : 0x82
	 * 1-4 : EntityID
	 */
	public static final byte IFI_Server_UpadteFigure	= (byte)0x82;

}
