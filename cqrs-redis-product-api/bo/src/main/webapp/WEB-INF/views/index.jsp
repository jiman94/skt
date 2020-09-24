<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="/resources/html/css/common.css"  />

<script src="/resources/html/js/vue/vue.js"></script>
<script src="/resources/html/js/vue/vue-friendly-iframe.js"></script>
<script src="/resources/html/js/vue/axios.min.js"></script>
	
<body></body>
<div align="center" id="app">
		<table>
			<tr>
				<td colspan="3"><h3>주문내역</h3></td>
			</tr>
			<tr>
				<td colspan="2" align="left" height="50"><strong><font size="2">data</font></strong></td>
				<td>
					<input type="button" @click="getdata" value="조회"/>
				</td>
			</tr>
			<tr><td><br><h4>결과 : {{ rsltmsg }}</h4></td></tr>
			<tr><td colspan="4"><br><hr></td></tr>
		</table>
		<table>
			<tr>
				<th width="100"> 주문번호 </th>
				<th width="100"> 제품번호 </th>
				<th width="100"> 제품명 </th>
				<th width="100"> 제품제고 </th>
				<th width="100"> 제품가격 </th>
				<th width="100"> 주문수량 </th>
				<th width="100"> 주문금액 </th>
			</tr>
			<template v-for='value in orderdata'>
				<tr v-for="item in value.orderItems">
					<td align="center">	{{ value.orderId }}	</td>
					<td align="center">	{{ item.productId }} </td>
					<td align="center">	{{ item.product.name }}	</td>
					<td align="center">	{{ item.product.inventory }} </td>
					<td align="right"> {{ item.product.formatPrice }} </td>
					<td align="center"> {{ item.quantity }} </td>
					<td align="right"> {{ item.formatProductPrice }} </td>
				</tr>
			</template>
		</table>
</div>


<script>
const app = new Vue({
	  el:'#app',
	  data:{
		orderdata:{},
		rsltmsg:''
	  },
	  mounted: function () {
		  this.getdata()
	  },
	  filters: {
	  },
	  computed: {
	  },
	  methods:{
		getdata:function(e) {
			this.rsltmsg = '처리 중';
			axios({
				method: 'GET',
				url : 'http://localhost:20001/api/orderlist',
				contentType : "application/x-www-form-urlencoded;charset=utf-8"
			}).then((response) => {
				if(response.status == "200"){
					this.orderdata = response.data;
					this.rsltmsg = '조회 성공';
				}else{
					this.rsltmsg = '조회 실패';
				}
			}).catch((ex)=> {
				console.log("ERR!!!!! : ", ex)
			})
			
		}
  }
})

</script>