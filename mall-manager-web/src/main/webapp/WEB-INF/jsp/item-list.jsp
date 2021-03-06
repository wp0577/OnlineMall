<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemList" title="商品列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/item/list',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">ITEM ID</th>
            <th data-options="field:'title',width:200">ITEM TITLE</th>
            <th data-options="field:'cid',width:100">CID</th>
            <th data-options="field:'sellPoint',width:100">SELL POINT</th>
            <th data-options="field:'price',width:70,align:'right',formatter:E3.formatPrice">PRICE</th>
            <th data-options="field:'num',width:70,align:'right'">STOCK VOLUME</th>
            <th data-options="field:'barcode',width:100">CARCODE</th>
            <th data-options="field:'status',width:60,align:'center',formatter:E3.formatItemStatus">STATUS</th>
            <th data-options="field:'created',width:130,align:'center',formatter:E3.formatDateTime">CREATE DATE</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:E3.formatDateTime">UPDATE DATE</th>
        </tr>
    </thead>
</table>
<div id="itemEditWindow" class="easyui-window" title="Edit Item" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/rest/page/item-edit'" style="width:80%;height:80%;padding:10px;">
</div>
<script>

    function getSelectionsIds(){
    	var itemList = $("#itemList");
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }

    var toolbar = [{
        text:'add',
        iconCls:'icon-add',
        handler:function(){
            //找menu tree中主题包含Add Item的内容，然后找到父级就是<li data-options="attributes:{'url':'item-add'}">Add Item</li>中的<li></li>
        	$(".tree-title:contains('Add Item')").parent().click();
        }
    },{
        text:'edit',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('warning','You should choose at least one item!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('warning','Only choose one item!');
        		return ;
        	}
        	
        	$("#itemEditWindow").window({
        		onLoad :function(){
        			//回显数据
        			var data = $("#itemList").datagrid("getSelections")[0];
        			data.priceView = E3.formatPrice(data.price);
        			$("#itemeEditForm").form("load",data);
        			
        			// 加载商品描述
        			$.getJSON('/rest/item/query/item/desc/'+data.id,function(_data){
        				if(_data.status == 200){
        					//UM.getEditor('itemeEditDescEditor').setContent(_data.data.itemDesc, false);
        					itemEditEditor.html(_data.data.itemDesc);
        				}
        			});
        			
        			//加载商品规格
        			$.getJSON('/rest/item/param/item/query/'+data.id,function(_data){
        				if(_data && _data.status == 200 && _data.data && _data.data.paramData){
        					$("#itemeEditForm .params").show();
        					$("#itemeEditForm [name=itemParams]").val(_data.data.paramData);
        					$("#itemeEditForm [name=itemParamId]").val(_data.data.id);
        					
        					//回显商品规格
        					 var paramData = JSON.parse(_data.data.paramData);
        					
        					 var html = "<ul>";
        					 for(var i in paramData){
        						 var pd = paramData[i];
        						 html+="<li><table>";
        						 html+="<tr><td colspan=\"2\" class=\"group\">"+pd.group+"</td></tr>";
        						 
        						 for(var j in pd.params){
        							 var ps = pd.params[j];
        							 html+="<tr><td class=\"param\"><span>"+ps.k+"</span>: </td><td><input autocomplete=\"off\" type=\"text\" value='"+ps.v+"'/></td></tr>";
        						 }
        						 
        						 html+="</li></table>";
        					 }
        					 html+= "</ul>";
        					 $("#itemeEditForm .params td").eq(1).html(html);
        				}
        			});
        			
        			E3.init({
        				"pics" : data.image,
        				"cid" : data.cid,
        				fun:function(node){
        					E3.changeItemParam(node, "itemeEditForm");
        				}
        			});
        		}
        	}).window("open");
        }
    },{
        text:'delete',
        iconCls:'icon-cancel',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('warning','No item select!');
        		return ;
        	}
        	$.messager.confirm('confirm','Are u sure to delete id: '+ids+' item？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/rest/item/delete",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('warning','Item delete successd!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    },'-',{
        text:'ItemDownshelf',
        iconCls:'icon-remove',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('warning','No item selected!');
        		return ;
        	}
        	$.messager.confirm('confirm','Are u sure to remove id: '+ids+' item from its shelves？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/rest/item/reshelf",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('ok','ItemDownShelf successd!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    },{
        text:'ItemUpshelf',
        iconCls:'icon-remove',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('warning','No item selected!');
        		return ;
        	}
        	$.messager.confirm('confirm','Are u sure to put id: '+ids+' item on sale ？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/rest/item/instock",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('ok','ItemUpShelf successd!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    }];
</script>