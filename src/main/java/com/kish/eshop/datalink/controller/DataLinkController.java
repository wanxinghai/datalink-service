package com.kish.eshop.datalink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSONObject;
import com.kish.eshop.datalink.service.EshopProductService;

@RestController
public class DataLinkController {
	
	@Autowired
	private EshopProductService eshopProductService;
	@Autowired
	private JedisPool jedisPool;
	
	@RequestMapping("/product")
	public String getProduct(Long productId){
		//先读本地的ehcache,但是这边不做了，因为之前做过了
		
		//读jedis主集群
		Jedis jedis = jedisPool.getResource();
		String dimProductJSON = jedis.get("dim_product_"+productId);
		
		if(dimProductJSON==null||"".equals(dimProductJSON)){
			String productDataJSON = eshopProductService.findProductById(productId);
	    	
	    	if(productDataJSON != null && !"".equals(productDataJSON)) {
	    		JSONObject productDataJSONObject = JSONObject.parseObject(productDataJSON);
	    		
	    		String productPropertyDataJSON = eshopProductService.findProductPropertyByProductId(productId);
	    		if(productPropertyDataJSON != null && !"".equals(productPropertyDataJSON)) {
	    			productDataJSONObject.put("product_property", JSONObject.parse(productPropertyDataJSON));
	    		} 
	    		
	    		String productSpecificationDataJSON = eshopProductService.findProductSpecificationByProductId(productId);
	    		if(productSpecificationDataJSON != null && !"".equals(productSpecificationDataJSON)) {
	    			productDataJSONObject.put("product_specification", JSONObject.parse(productSpecificationDataJSON));
	    		}
	    		
	    		jedis.set("dim_product_" + productId, productDataJSONObject.toJSONString());
	    		
	    		return productDataJSONObject.toJSONString();
	    	} 
		}else{
			return dimProductJSON;
		}
		return "";
	}
}
