/**
 * Calendar component
 * 
 * @screen Common
 * @author li_rubin
 */
Ext.define('Ext.ux.calendar.CompanyCalendar', {

	extend : 'Ext.panel.Panel',

	alternateClassName : 'Ext.ux.CompanyCalendar',
	alias : ['Ext.ux.CompanyCalendar'],
	
    width : 1052,
    border : false,
    
	calendarYear : '',
	WEEKS : ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
	MONTHS : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
	WEEKENDS : {},
	NONWORKINGDAYS : {},
	EXISTFLAG : 1,
	
	dockedItems: [
		{
            xtype: 'toolbar',
            itemId : 'bottombar',
            dock: 'bottom',
            hidden : true,
            margin : '5 0 0 0',
            border : false,
            items: [
                { 
                    xtype : 'checkboxfield',
                    name : 'checkbox',
                    margin : '0 0 0 20',
                    handler : function(thisObj,checked){
                    	var calendarCmp = this.up('#bottombar').up();
                    	calendarCmp.markWeekends(checked);
                    }
                },{
                    xtype : 'label',
                    margin : '0 0 0 20',
                    text: chinaplus.label.CPMCMS01_Label_CHK
                }
            ]
        }
    ],
	
	initComponent : function () {
		this.WEEKENDS = {};
        this.NONWORKINGDAYS = {};
		this.callParent();
		this.EXISTFLAG = 1;
	},
	setYear : function(year){
	   var thisObj = this;
	   thisObj.calendarYear = year;
	},
	getYear : function(){
	   var thisObj = this;
	   return new Number(thisObj.calendarYear);
	},
	/***
	 * @param {} nonworkingDays ['yyyy-mm-dd']
	 */
	loadCalendar : function(nonworkingDays, existFlag){
		var thisObj = this;
		thisObj.WEEKENDS = {};
		thisObj.NONWORKINGDAYS = {};
		thisObj.down('#bottombar').down('checkbox').reset();
		thisObj.EXISTFLAG = existFlag;
	    if(Ext.isNumeric(thisObj.calendarYear)){
            thisObj.calendarYear = new Number(thisObj.calendarYear);
            if(nonworkingDays&&Ext.isArray(nonworkingDays)&&nonworkingDays.length>0){
                for(var i in nonworkingDays){
                    thisObj.NONWORKINGDAYS[nonworkingDays[i]] = 1;
                }
            }
            this.initTemplates();
        }
	},
	initTemplates : function () {
        var thisObj = this;
        var year = thisObj.calendarYear;
        var html = '<table class="ux-companyCalendar-table"><tr><td colspan="21" class="ux-companyCalendar-calendarYear"><a href="#" style="text-decoration:none;" onclick="{lastyearEvent}">&lt;&lt;</a>&nbsp;&nbsp;&nbsp;&nbsp;{calendarYear}&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" style="text-decoration:none;" onclick="{nextyearEvent}">&gt;&gt;</a></td></tr><tr><th colspan="7">Jan</th><th colspan="7">Feb</th><th colspan="7">Mar</th></tr><tr><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th></tr><tr><td>Jan{1}</td><td>Jan{2}</td><td>Jan{3}</td><td>Jan{4}</td><td>Jan{5}</td><td>Jan{6}</td><td>Jan{7}</td><td>Feb{1}</td><td>Feb{2}</td><td>Feb{3}</td><td>Feb{4}</td><td>Feb{5}</td><td>Feb{6}</td><td>Feb{7}</td><td>Mar{1}</td><td>Mar{2}</td><td>Mar{3}</td><td>Mar{4}</td><td>Mar{5}</td><td>Mar{6}</td><td>Mar{7}</td></tr><tr><td>Jan{8}</td><td>Jan{9}</td><td>Jan{10}</td><td>Jan{11}</td><td>Jan{12}</td><td>Jan{13}</td><td>Jan{14}</td><td>Feb{8}</td><td>Feb{9}</td><td>Feb{10}</td><td>Feb{11}</td><td>Feb{12}</td><td>Feb{13}</td><td>Feb{14}</td><td>Mar{8}</td><td>Mar{9}</td><td>Mar{10}</td><td>Mar{11}</td><td>Mar{12}</td><td>Mar{13}</td><td>Mar{14}</td></tr><tr><td>Jan{15}</td><td>Jan{16}</td><td>Jan{17}</td><td>Jan{18}</td><td>Jan{19}</td><td>Jan{20}</td><td>Jan{21}</td><td>Feb{15}</td><td>Feb{16}</td><td>Feb{17}</td><td>Feb{18}</td><td>Feb{19}</td><td>Feb{20}</td><td>Feb{21}</td><td>Mar{15}</td><td>Mar{16}</td><td>Mar{17}</td><td>Mar{18}</td><td>Mar{19}</td><td>Mar{20}</td><td>Mar{21}</td></tr><tr><td>Jan{22}</td><td>Jan{23}</td><td>Jan{24}</td><td>Jan{25}</td><td>Jan{26}</td><td>Jan{27}</td><td>Jan{28}</td><td>Feb{22}</td><td>Feb{23}</td><td>Feb{24}</td><td>Feb{25}</td><td>Feb{26}</td><td>Feb{27}</td><td>Feb{28}</td><td>Mar{22}</td><td>Mar{23}</td><td>Mar{24}</td><td>Mar{25}</td><td>Mar{26}</td><td>Mar{27}</td><td>Mar{28}</td></tr><tr><td>Jan{29}</td><td>Jan{30}</td><td>Jan{31}</td><td>Jan{32}</td><td>Jan{33}</td><td>Jan{34}</td><td>Jan{35}</td><td>Feb{29}</td><td>Feb{30}</td><td>Feb{31}</td><td>Feb{32}</td><td>Feb{33}</td><td>Feb{34}</td><td>Feb{35}</td><td>Mar{29}</td><td>Mar{30}</td><td>Mar{31}</td><td>Mar{32}</td><td>Mar{33}</td><td>Mar{34}</td><td>Mar{35}</td></tr><tr><td>Jan{36}</td><td>Jan{37}</td><td>Jan{38}</td><td>Jan{39}</td><td>Jan{40}</td><td>Jan{41}</td><td>Jan{42}</td><td>Feb{36}</td><td>Feb{37}</td><td>Feb{38}</td><td>Feb{39}</td><td>Feb{40}</td><td>Feb{41}</td><td>Feb{42}</td><td>Mar{36}</td><td>Mar{37}</td><td>Mar{38}</td><td>Mar{39}</td><td>Mar{40}</td><td>Mar{41}</td><td>Mar{42}</td></tr><tr><th colspan="7">Apr</th><th colspan="7">May</th><th colspan="7">Jun</th></tr><tr><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th></tr><tr><td>Apr{1}</td><td>Apr{2}</td><td>Apr{3}</td><td>Apr{4}</td><td>Apr{5}</td><td>Apr{6}</td><td>Apr{7}</td><td>May{1}</td><td>May{2}</td><td>May{3}</td><td>May{4}</td><td>May{5}</td><td>May{6}</td><td>May{7}</td><td>Jun{1}</td><td>Jun{2}</td><td>Jun{3}</td><td>Jun{4}</td><td>Jun{5}</td><td>Jun{6}</td><td>Jun{7}</td></tr><tr><td>Apr{8}</td><td>Apr{9}</td><td>Apr{10}</td><td>Apr{11}</td><td>Apr{12}</td><td>Apr{13}</td><td>Apr{14}</td><td>May{8}</td><td>May{9}</td><td>May{10}</td><td>May{11}</td><td>May{12}</td><td>May{13}</td><td>May{14}</td><td>Jun{8}</td><td>Jun{9}</td><td>Jun{10}</td><td>Jun{11}</td><td>Jun{12}</td><td>Jun{13}</td><td>Jun{14}</td></tr><tr><td>Apr{15}</td><td>Apr{16}</td><td>Apr{17}</td><td>Apr{18}</td><td>Apr{19}</td><td>Apr{20}</td><td>Apr{21}</td><td>May{15}</td><td>May{16}</td><td>May{17}</td><td>May{18}</td><td>May{19}</td><td>May{20}</td><td>May{21}</td><td>Jun{15}</td><td>Jun{16}</td><td>Jun{17}</td><td>Jun{18}</td><td>Jun{19}</td><td>Jun{20}</td><td>Jun{21}</td></tr><tr><td>Apr{22}</td><td>Apr{23}</td><td>Apr{24}</td><td>Apr{25}</td><td>Apr{26}</td><td>Apr{27}</td><td>Apr{28}</td><td>May{22}</td><td>May{23}</td><td>May{24}</td><td>May{25}</td><td>May{26}</td><td>May{27}</td><td>May{28}</td><td>Jun{22}</td><td>Jun{23}</td><td>Jun{24}</td><td>Jun{25}</td><td>Jun{26}</td><td>Jun{27}</td><td>Jun{28}</td></tr><tr><td>Apr{29}</td><td>Apr{30}</td><td>Apr{31}</td><td>Apr{32}</td><td>Apr{33}</td><td>Apr{34}</td><td>Apr{35}</td><td>May{29}</td><td>May{30}</td><td>May{31}</td><td>May{32}</td><td>May{33}</td><td>May{34}</td><td>May{35}</td><td>Jun{29}</td><td>Jun{30}</td><td>Jun{31}</td><td>Jun{32}</td><td>Jun{33}</td><td>Jun{34}</td><td>Jun{35}</td></tr><tr><td>Apr{36}</td><td>Apr{37}</td><td>Apr{38}</td><td>Apr{39}</td><td>Apr{40}</td><td>Apr{41}</td><td>Apr{42}</td><td>May{36}</td><td>May{37}</td><td>May{38}</td><td>May{39}</td><td>May{40}</td><td>May{41}</td><td>May{42}</td><td>Jun{36}</td><td>Jun{37}</td><td>Jun{38}</td><td>Jun{39}</td><td>Jun{40}</td><td>Jun{41}</td><td>Jun{42}</td></tr><tr><th colspan="7">Jul</th><th colspan="7">Aug</th><th colspan="7">Sep</th></tr><tr><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th></tr><tr><td>Jul{1}</td><td>Jul{2}</td><td>Jul{3}</td><td>Jul{4}</td><td>Jul{5}</td><td>Jul{6}</td><td>Jul{7}</td><td>Aug{1}</td><td>Aug{2}</td><td>Aug{3}</td><td>Aug{4}</td><td>Aug{5}</td><td>Aug{6}</td><td>Aug{7}</td><td>Sep{1}</td><td>Sep{2}</td><td>Sep{3}</td><td>Sep{4}</td><td>Sep{5}</td><td>Sep{6}</td><td>Sep{7}</td></tr><tr><td>Jul{8}</td><td>Jul{9}</td><td>Jul{10}</td><td>Jul{11}</td><td>Jul{12}</td><td>Jul{13}</td><td>Jul{14}</td><td>Aug{8}</td><td>Aug{9}</td><td>Aug{10}</td><td>Aug{11}</td><td>Aug{12}</td><td>Aug{13}</td><td>Aug{14}</td><td>Sep{8}</td><td>Sep{9}</td><td>Sep{10}</td><td>Sep{11}</td><td>Sep{12}</td><td>Sep{13}</td><td>Sep{14}</td></tr><tr><td>Jul{15}</td><td>Jul{16}</td><td>Jul{17}</td><td>Jul{18}</td><td>Jul{19}</td><td>Jul{20}</td><td>Jul{21}</td><td>Aug{15}</td><td>Aug{16}</td><td>Aug{17}</td><td>Aug{18}</td><td>Aug{19}</td><td>Aug{20}</td><td>Aug{21}</td><td>Sep{15}</td><td>Sep{16}</td><td>Sep{17}</td><td>Sep{18}</td><td>Sep{19}</td><td>Sep{20}</td><td>Sep{21}</td></tr><tr><td>Jul{22}</td><td>Jul{23}</td><td>Jul{24}</td><td>Jul{25}</td><td>Jul{26}</td><td>Jul{27}</td><td>Jul{28}</td><td>Aug{22}</td><td>Aug{23}</td><td>Aug{24}</td><td>Aug{25}</td><td>Aug{26}</td><td>Aug{27}</td><td>Aug{28}</td><td>Sep{22}</td><td>Sep{23}</td><td>Sep{24}</td><td>Sep{25}</td><td>Sep{26}</td><td>Sep{27}</td><td>Sep{28}</td></tr><tr><td>Jul{29}</td><td>Jul{30}</td><td>Jul{31}</td><td>Jul{32}</td><td>Jul{33}</td><td>Jul{34}</td><td>Jul{35}</td><td>Aug{29}</td><td>Aug{30}</td><td>Aug{31}</td><td>Aug{32}</td><td>Aug{33}</td><td>Aug{34}</td><td>Aug{35}</td><td>Sep{29}</td><td>Sep{30}</td><td>Sep{31}</td><td>Sep{32}</td><td>Sep{33}</td><td>Sep{34}</td><td>Sep{35}</td></tr><tr><td>Jul{36}</td><td>Jul{37}</td><td>Jul{38}</td><td>Jul{39}</td><td>Jul{40}</td><td>Jul{41}</td><td>Jul{42}</td><td>Aug{36}</td><td>Aug{37}</td><td>Aug{38}</td><td>Aug{39}</td><td>Aug{40}</td><td>Aug{41}</td><td>Aug{42}</td><td>Sep{36}</td><td>Sep{37}</td><td>Sep{38}</td><td>Sep{39}</td><td>Sep{40}</td><td>Sep{41}</td><td>Sep{42}</td></tr><tr><th colspan="7">Oct</th><th colspan="7">Nov</th><th colspan="7">Dec</th></tr><tr><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th><th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th></tr><tr><td>Oct{1}</td><td>Oct{2}</td><td>Oct{3}</td><td>Oct{4}</td><td>Oct{5}</td><td>Oct{6}</td><td>Oct{7}</td><td>Nov{1}</td><td>Nov{2}</td><td>Nov{3}</td><td>Nov{4}</td><td>Nov{5}</td><td>Nov{6}</td><td>Nov{7}</td><td>Dec{1}</td><td>Dec{2}</td><td>Dec{3}</td><td>Dec{4}</td><td>Dec{5}</td><td>Dec{6}</td><td>Dec{7}</td></tr><tr><td>Oct{8}</td><td>Oct{9}</td><td>Oct{10}</td><td>Oct{11}</td><td>Oct{12}</td><td>Oct{13}</td><td>Oct{14}</td><td>Nov{8}</td><td>Nov{9}</td><td>Nov{10}</td><td>Nov{11}</td><td>Nov{12}</td><td>Nov{13}</td><td>Nov{14}</td><td>Dec{8}</td><td>Dec{9}</td><td>Dec{10}</td><td>Dec{11}</td><td>Dec{12}</td><td>Dec{13}</td><td>Dec{14}</td></tr><tr><td>Oct{15}</td><td>Oct{16}</td><td>Oct{17}</td><td>Oct{18}</td><td>Oct{19}</td><td>Oct{20}</td><td>Oct{21}</td><td>Nov{15}</td><td>Nov{16}</td><td>Nov{17}</td><td>Nov{18}</td><td>Nov{19}</td><td>Nov{20}</td><td>Nov{21}</td><td>Dec{15}</td><td>Dec{16}</td><td>Dec{17}</td><td>Dec{18}</td><td>Dec{19}</td><td>Dec{20}</td><td>Dec{21}</td></tr><tr><td>Oct{22}</td><td>Oct{23}</td><td>Oct{24}</td><td>Oct{25}</td><td>Oct{26}</td><td>Oct{27}</td><td>Oct{28}</td><td>Nov{22}</td><td>Nov{23}</td><td>Nov{24}</td><td>Nov{25}</td><td>Nov{26}</td><td>Nov{27}</td><td>Nov{28}</td><td>Dec{22}</td><td>Dec{23}</td><td>Dec{24}</td><td>Dec{25}</td><td>Dec{26}</td><td>Dec{27}</td><td>Dec{28}</td></tr><tr><td>Oct{29}</td><td>Oct{30}</td><td>Oct{31}</td><td>Oct{32}</td><td>Oct{33}</td><td>Oct{34}</td><td>Oct{35}</td><td>Nov{29}</td><td>Nov{30}</td><td>Nov{31}</td><td>Nov{32}</td><td>Nov{33}</td><td>Nov{34}</td><td>Nov{35}</td><td>Dec{29}</td><td>Dec{30}</td><td>Dec{31}</td><td>Dec{32}</td><td>Dec{33}</td><td>Dec{34}</td><td>Dec{35}</td></tr><tr><td>Oct{36}</td><td>Oct{37}</td><td>Oct{38}</td><td>Oct{39}</td><td>Oct{40}</td><td>Oct{41}</td><td>Oct{42}</td><td>Nov{36}</td><td>Nov{37}</td><td>Nov{38}</td><td>Nov{39}</td><td>Nov{40}</td><td>Nov{41}</td><td>Nov{42}</td><td>Dec{36}</td><td>Dec{37}</td><td>Dec{38}</td><td>Dec{39}</td><td>Dec{40}</td><td>Dec{41}</td><td>Dec{42}</td></tr></table>';
        var blankTableRow = '<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>';
        html = html.replace('{calendarYear}',year);
        html = html.replace('{lastyearEvent}','Ext.getCmp(\'' + thisObj.id + '\')._lastYear()');
        html = html.replace('{nextyearEvent}','Ext.getCmp(\'' + thisObj.id + '\')._nextYear()');
        if(Ext.Object.isEmpty(thisObj.NONWORKINGDAYS)){
        	if (thisObj.EXISTFLAG == 0) {
        		 html = html.replace('ux-companyCalendar-calendarYear','ux-companyCalendar-calendarYear-red');
            }
        }
        var shortMonth;
        var week;
        var dayOfWeek;
        var dayOfMonth;
        var replace;
        var reg;
        var temp;
        var yyyyMMdd;
        var date = new Date();
        if(Ext.Object.isEmpty(thisObj.NONWORKINGDAYS)){
            for (var i = 0; i < 12; i++) {
                shortMonth = thisObj.MONTHS[i];
                date.setFullYear(year, i, 1);
                dayOfWeek = date.getDay() + 1;
                for (var j = dayOfWeek; j <= 42 && date.getMonth() == i; j++) {
                    dayOfMonth = date.getDate();
                    yyyyMMdd = Ext.Date.format(date,'Y-m-d');
                    replace = '>' + shortMonth + '{' + j + '}';
                    temp = ' onclick="Ext.getCmp(\'' + thisObj.id + '\').cellClick(this)" id="Y-m-d_'+ yyyyMMdd +'" style="cursor:pointer">' + dayOfMonth;
                    if(date.getDay()==0||date.getDay()==6){
                        thisObj.WEEKENDS[yyyyMMdd] = 1;
					}
					if (thisObj.EXISTFLAG == 0) {
						if(date.getDay()==0||date.getDay()==6){
                        thisObj.NONWORKINGDAYS[yyyyMMdd] = 1;
                        temp = ' class="ux-companyCalendar-weekend"' + temp;
                    }
					}
                    html = html.replace(replace, temp);
                    
                    date.setFullYear(year, i, dayOfMonth + 1);
                }
                reg = new RegExp(shortMonth + '\{[0-9]+\}', 'g');
                html = html.replace(reg, '');
                html = html.replace(shortMonth, TRF.util.getLabel('CPMCMS01_Label_' + shortMonth));
                for(var w = 0; w<= 6; w++){
                	week = thisObj.WEEKS[w];
                	html = html.replace(week, TRF.util.getLabel('CPMCMS01_Label_' +'Cal_' + week));
                }
            }
        }else{
            for (var i = 0; i < 12; i++) {
                shortMonth = thisObj.MONTHS[i];
                date.setFullYear(year, i, 1);
                dayOfWeek = date.getDay() + 1;
                for (var j = dayOfWeek; j <= 42 && date.getMonth() == i; j++) {
                    dayOfMonth = date.getDate();
                    yyyyMMdd = Ext.Date.format(date,'Y-m-d');
                    if(date.getDay()==0||date.getDay()==6){
                        thisObj.WEEKENDS[yyyyMMdd] = 1;
                    }
                    replace = '>' + shortMonth + '{' + j + '}';
                    temp = ' onclick="Ext.getCmp(\'' + thisObj.id + '\').cellClick(this)" id="Y-m-d_'+ yyyyMMdd +'" style="cursor:pointer">' + dayOfMonth;
                    if(thisObj.NONWORKINGDAYS[yyyyMMdd]==1){
                        temp = ' class="ux-companyCalendar-weekend"' + temp;
                    }
                    html = html.replace(replace, temp);
                    
                    date.setFullYear(year, i, dayOfMonth + 1);
                }
                reg = new RegExp(shortMonth + '\{[0-9]+\}', 'g');
                html = html.replace(reg, '');
                html = html.replace(shortMonth, TRF.util.getLabel('CPMCMS01_Label_' + shortMonth));
                for(var w = 0; w<= 6; w++){
                	week = thisObj.WEEKS[w];
                	html = html.replace(week, TRF.util.getLabel('CPMCMS01_Label_' +'Cal_' + week));
                }
            }
        }
		reg = new RegExp(blankTableRow,'g');
		html = html.replace(reg, '');
		this.update(html);
	},
	getNonWorkingDay : function(){
		var thisObj = this;
		return Ext.Object.getKeys(thisObj.NONWORKINGDAYS);
	},
	cellClick : function(cellEl){
	   var thisObj = this;
	   if(cellEl&&Ext.isString(cellEl.id)){
	   	  var prefix = 'Y-m-d_';
	      var yyyyMMdd = cellEl.id.replace(prefix,'');
	      if(thisObj.NONWORKINGDAYS[yyyyMMdd]==1){
	          delete thisObj.NONWORKINGDAYS[yyyyMMdd];
	          cellEl.className = '';
	      }else{
	          thisObj.NONWORKINGDAYS[yyyyMMdd] = 1;
	          cellEl.className = 'ux-companyCalendar-weekend';
	      }
	   }
	},
	onLastYear : function(year){
	   
	},
	onNextYear : function(year){
	   
	},
	_lastYear : function(){
		var thisObj = this;
	    var year = thisObj.calendarYear;
        if(Ext.isNumeric(year)){
            year = new Number(year) - 1;
            thisObj.calendarYear = year;
            thisObj.onLastYear(year);
        }
	},
	_nextYear : function(){
	    var thisObj = this;
        var year = thisObj.calendarYear;
        if(Ext.isNumeric(year)){
            year = new Number(year) + 1;
            thisObj.calendarYear = year;
            thisObj.onNextYear(year);
        }
	},
	markWeekends : function(checked) {
		var thisObj = this;
		var table = thisObj.body.down('table');
		var prefix = 'Y-m-d_';
		var arr = Ext.Object.getKeys(thisObj.WEEKENDS);
		var length = arr.length;
		var cellId;
		if(checked){
		    for(var i =0;i<length;i++){
                cellId = prefix + arr[i];
                table.getById(cellId, true).className = '';
                delete thisObj.NONWORKINGDAYS[arr[i]];
            }
		}else{
            for(var i =0;i<length;i++){
                cellId = prefix + arr[i];
                table.getById(cellId, true).className = 'ux-companyCalendar-weekend';
                thisObj.NONWORKINGDAYS[arr[i]] = 1;
            }
		}
	}
});
