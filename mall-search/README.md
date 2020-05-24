elasticsearch选择7.6版本，刚开始使用6.8.8出现不能创建的情况，后续去研究是为什么？

```cmd
org.elasticsearch.ElasticsearchException: Elasticsearch exception 
[type=illegal_argument_exception, reason=Can't load fielddata on 
[brandName] because fielddata is unsupported on fields of type [keyword]. 
Use doc values instead.]
```
故而下属映射中的doc_values设置为true
# 创建映射
```
PUT product 
{
  "mappings": {
    "properties":{
      "skuId":{
        "type": "long"
      },
      "spuId":{
        "type": "keyword"
      },
      "skuTitle":{
        "type": "text", 
        "analyzer": "standard"
      },
      "skuPrice":{
        "type": "keyword"
      },
      "skuImg":{
        "type": "keyword",
        "index": false,
        "doc_values":true
      },
      "saleCount":{
        "type": "long"
      },
      "hasStock":{
        "type": "boolean"
      },
      "hotScore":{
        "type": "long"
      },
      "catalogId":{
        "type": "long"
      },
      "brandName":{
        "type": "keyword",
        "index": false,
        "doc_values":true
     },
     "brandImg":{
        "type": "keyword",
        "index": false,
         "doc_values":true
     },
     "catalogName":{
        "type": "keyword",
        "index": false,
         "doc_values":true
     },
     "attrs":{
       "type": "nested",
       "properties": {
         "attrId":{
           "type":"long"
         },
         "attrName":{
            "type": "keyword",
            "index": false,
             "doc_values":true
         },
         "attrValue":{
           "type": "keyword"
         }
       }
     }
  }
 }
}
```
# 查询
```
GET product/_search
```

# 查询2 【hasStock false】
```
GET product/_search
{"from":0,"size":16,"query":{"bool":{"filter":[{"term":{"hasStock":{"value":false,"boost":1.0}}}],"adjust_pure_negative":true,"boost":1.0}}}
```
URL测试：
```
http://localhost:12000/list.html?skuPrice=6000_&keyword=苹果&hasStock=1&attrs=15_高通(Qualcomm)&catalog3Id=225
```
