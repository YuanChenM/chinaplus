/**
 * chinaplus-cnt.js
 * 
 * @screen COMMON		
 * @author zhang_pingwu
 */
/** ******** const ********* */
Ext.namespace('chinaplus.consts', 'chinaplus.consts.code',
		'chinaplus.consts.array');

chinaplus.consts.code.UserStatus = {
	INACTIVE : 0,
	ACTIVE : 1,
	LOCKED : 9
};
chinaplus.consts.code.InvoiceMatchStatus = {
		MISMATCH : 1,
		MATCHED : 2
	};
chinaplus.consts.code.AccessLevel = {
	NONE : 0,
	LOW : 1,
	MED1 : 2,
	MED2 : 3,
	HIGH : 4,
	MAIN : 5
};
chinaplus.consts.code.AuthMethd = {
	HIDE : 0,
	READONLY : 1
};
chinaplus.consts.code.Language = {
	EN : 'en_US',
	CN : 'zh_CN'
};
chinaplus.consts.code.RootId = {
	COMMON : 'CPC00000',
	VV : 'CPV00000',
	AISIN : 'CPA00000',
	MANGER : 'CPM00000'
};

chinaplus.consts.array.Hour = [ "00", "01", "02", "03", "04", "05", "06", "07",
		"08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
		"20", "21", "22", "23" ];
chinaplus.consts.array.Minute = [ "00", "01", "02", "03", "04", "05", "06",
		"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
		"19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
		"31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42",
		"43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
		"55", "56", "57", "58", "59" ];
chinaplus.consts.array.Timezone = [ "0", "1", "2", "3", "4", "5", "6", "7",
		"8", "9", "10", "11", "12" ];

chinaplus.consts.code.RevisionReason = {
	OTHER : 999
};

chinaplus.consts.code.ImpStockFlag = {
	WITH_CLEARANCE : 1,
	WITHOUT_CLEARANCE : 2
};

chinaplus.consts.code.CustStockFlag = {
	Y : 1,
	N : 0
};

chinaplus.consts.code.InventoryByBox = {
	Y : 1,
	N : 0
};

chinaplus.consts.code.AlarmFlag = {
	Y : 1,
	N : 0
};

chinaplus.consts.code.NoCfcFlag = {
	Y : 1,
	N : 0
};

chinaplus.consts.code.NoPfcFlag = {
	Y : 1,
	N : 0
};

chinaplus.consts.code.BusinessPattern = {
	VV : 1,
	AISIN : 2
};