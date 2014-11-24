<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<jsp:include page="header.jsp?hideHeader=true" />

<script type="text/javascript">

<!--

var data = [["ABC Supplier", 150000], ["Tulsi Supplier", 200000]];

$(function() 
	{

$("#grid").jqGrid({
    datatype: "local",
    colNames: ['Supplier Name', 'Amount'],
    colModel: [{
        name: 'sname',
        index: 'sname',
        width: 300,
        sorttype: "int"},
       {
        name: 'amt',
        index: 'amt',
        width: 150,
        sorttype: "int"}    
    ],
    multiselect: true,
    pager: '#pager',
    viewrecords: true,
    sortname: 'sname',
    sortorder: "asc",
    caption: "Suppliers To Send Payment Today"
});

var names = ["sname", "amt"];
var mydata = [];

for (var i = 0; i < data.length; i++) {
    mydata[i] = {};
    for (var j = 0; j < data[i].length; j++) {
        mydata[i][names[j]] = data[i][j];
    }
}

for (var i = 0; i <= mydata.length; i++) {
    $("#grid").jqGrid('addRowData', i + 1, mydata[i]);
}
//$("#grid").jqGrid('navGrid','#pager',{edit:true,add:false,del:true});
//$("#grid").jqGrid('setGridParam', {ondblClickRow: function(rowid,iRow,iCol,e){alert('double clicked');}});

var grid = $("#grid"),
    myDelOptions = {
        // because I use "local" data I don't want to send the changes
        // to the server so I use "processing:true" setting and delete
        // the row manually in onclickSubmit
        onclickSubmit: function(options, rowid) {
            var grid_id = $.jgrid.jqID(grid[0].id),
                grid_p = grid[0].p,
                newPage = grid_p.page;

            // reset the value of processing option which could be modified
            options.processing = true;

            // delete the row
            grid.delRowData(rowid);
            $.jgrid.hideModal("#delmod"+grid_id,
                              {gb:"#gbox_"+grid_id,
                              jqm:options.jqModal,onClose:options.onClose});

            if (grid_p.lastpage > 1) {// on the multipage grid reload the grid
                if (grid_p.reccount === 0 && newPage === grid_p.lastpage) {
                    // if after deliting there are no rows on the current page
                    // which is the last page of the grid
                    newPage--; // go to the previous page
                }
                // reload grid to make the row from the next page visable.
                grid.trigger("reloadGrid", [{page:newPage}]);
            }

            return true;
        },
        processing:true
    };

grid.jqGrid('delGridRow', rowid, myDelOptions);

});

var $table = $('#grid');

function deleteRow(){
 getCurrentRow();
 var tableData = new Array();
 var ids = '';
 $table.delRowData($table.getGridParam('selrow'));
 ids = $table.getDataIDs();
 for(var i = 0; i < ids.length; i++){
 tableData[i] = $table.getRowData(ids[i]);
 tableData[i].id = i + 1;
 }
 $table.clearGridData(false);
 for(i = 0; i < tableData.length; i++){
 $table.addRowData(i + 1, tableData[i]);
 }
 }

function getCurrentRow(){
 var dataString = '';
 var ids = $table.getDataIDs();
 dataString += 'Selected Row: ' + $table.getGridParam('selrow') + '\nRow ID: ' + ids[$table.getGridParam('selrow') - 1];
 alert(dataString);
 }
//-->
</script>
  <div id="two">
    <div class="item">    
      <table width="100%" cellpadding="10" cellspacing="5">
		<tr>
			<td nowrap align="right">Supplier Name:</td>
			<td>
      			<select class="chosen-select" style="width:175px">
      				<option value="" selected disabled>Select Supplier</option>
      				<option>ABC Supplier</option>
      				<option>Bata</option>
      				<option>Tulsi Supplier</option>
      				<option>asssdfsdf</option>
      				<option>ojwerd</option>
      				<option>ssfg</option>
      				<option>sdfasdfasdf</option>
      				<option>pioio</option>
      			</select>
      		</td>
			<td width="100%">&nbsp;</td>
		</tr>
		<tr>
			<td nowrap>Payment Amount:</td>
			<td><input type="text" size="25px" style="padding:3px;"></td>
			<td width="100%">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<input type="button" value="Add To List">
			</td>
			<td width="100%">&nbsp;</td>
		</tr>
	</table>
  	</div>
  </div>

  <div id="one">
	<table id="grid"></table> 
	<div id="pager"></div>
	<input type="button" value="Delete Row" onclick="deleteRow();" /><br />
  	
  </div>	 		 
 <jsp:include page="footer.jsp" />