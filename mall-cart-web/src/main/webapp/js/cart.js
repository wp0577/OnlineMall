var CART = {
	itemNumChange : function(){
		$(".increment").click(function(){//＋
			var _thisInput = $(this).siblings("input");
			_thisInput.val(eval(_thisInput.val()) + 1);
			$.post("/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".action",function(data){
				CART.refreshTotalPrice();
				CART.refreshSinglePrice(_thisInput.attr("itemId"), _thisInput.val());
			});
		});
		$(".decrement").click(function(){//-
			var _thisInput = $(this).siblings("input");
			if(eval(_thisInput.val()) == 1){
				return ;
			}
			_thisInput.val(eval(_thisInput.val()) - 1);
			$.post("/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".action",function(data){
				CART.refreshTotalPrice();
                CART.refreshSinglePrice(_thisInput.attr("itemId"), _thisInput.val());
            });
		});
		/*$(".itemnum").change(function(){
			var _thisInput = $(this);
			alert("test2");
            alert($("#total_price").value);
			/!*$.post("/service/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val(),function(data){
				CART.refreshTotalPrice();
			});*!/
		});*/
	},
	refreshTotalPrice : function(){ //重新计算总价
		var total = 0;
		$(".itemnum").each(function(i,e){
			var _this = $(e);
			total += (eval(_this.attr("itemPrice")) * 10000 * eval(_this.val())) / 10000;
		});
		$("#allMoney2").html(new Number(total/100).toFixed(2)).priceFormat({ //价格格式化插件
			 prefix: '¥',
			 thousandsSeparator: ',',
			 centsLimit: 2
		});
	},
    refreshSinglePrice : function(itemId, num){ //重新计算单价
		//对所有class为totalprice的元素进行遍历
        $(".totalprice").each(function (i,e) {
        	//将元素变为jquery对象
            var _this = $(e);
            //alert(eval(_this.attr("itemId")));
			//取与更改时的商品id相同的对象
            if(eval(_this.attr("itemId") == itemId)) {
            	//_this.val((eval(_this.attr("itemPrice"))) / 100 *num);
				var total = eval(_this.attr("itemPrice"));
				//alert(total);
                _this.html(new Number(total/100 * num).toFixed(2)).priceFormat({ //价格格式化插件
                    prefix: '¥',
                    thousandsSeparator: ',',
                    centsLimit: 2
                });
			}
        })
    },
    /*refreshSinglePrice : function(){ //重新计算小计
        var total = 2000;
        $(".itemnum").each(function(i,e){
            var _this = $(e);
            //alert(eval(_this.attr("itemId")));
			//alert(eval($("#total_price").attr("itemId")));
			alert(eval(_this.parents(".clearit").children(".pSubtotal").children("#total_price").attr("itemId")));
            if(eval($(".totalprice").attr("itemId") == _this.attr("itemId"))) {
            	//alert(_this.attr("itemId"));
            }
            /!*$(".totalprice").html(new Number(_this.attr("itemPrice")).toFixed(2)).priceFormat({ //价格格式化插件
                prefix: '¥',
                thousandsSeparator: ',',
                centsLimit: 2
            });*!/
            /!*total += (eval(_this.attr("itemPrice")) * 10000 * eval(_this.val())) / 10000;*!/
        });
        /!*$("#allMoney2").html(new Number(total/100).toFixed(2)).priceFormat({ //价格格式化插件
            prefix: '¥',
            thousandsSeparator: ',',
            centsLimit: 2
        });*!/
    }*/
};

$(function(){
	CART.itemNumChange();
});