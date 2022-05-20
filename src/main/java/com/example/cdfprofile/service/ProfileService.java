package com.example.cdfprofile.service;

import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Service
public class ProfileService {
    @Autowired
    RestHighLevelClient client;
    Logger logger = LoggerFactory.getLogger(this.getClass());
//Map<Long,Integer>
    public  Map<Long,Integer> getCountProfile() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.existsQuery("data.EMAIL"))
                .should(QueryBuilders.existsQuery("data.MOBILE")).minimumShouldMatch(1);

        searchSourceBuilder.query(queryBuilder);
        String[] includeFields = new String[] {"source_id"};
        String[] excludeFields = new String[]{};
        searchSourceBuilder.fetchSource(includeFields,excludeFields);
        SearchRequest searchRequest = new SearchRequest("cdp-profiles-*");
        searchRequest.source(searchSourceBuilder);
        Map<Long, Integer> mapResult = new HashMap<Long, Integer>();

        searchRequest.scroll(TimeValue.timeValueMinutes(1));
        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
        String scrollId = searchResponse.getScrollId();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        //Xu ly ket qua tra ve
        for(SearchHit hit : searchHits) {
            Long source =(Long) hit.getSourceAsMap().get("source_id");
            if(mapResult.get(source) == null) mapResult.put(source,1);
            else mapResult.put(source,mapResult.get(source)+1);
        }

        while (searchHits != null && searchHits.length >0)
        {

            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(TimeValue.timeValueSeconds(10));
            SearchResponse searchScrollResponse = client.scroll(scrollRequest,RequestOptions.DEFAULT);
            scrollId = searchScrollResponse.getScrollId();
            searchHits = searchScrollResponse.getHits().getHits();
            for(SearchHit hit : searchHits) {
                Long source =(Long) hit.getSourceAsMap().get("source_id");
                if(mapResult.get(source) == null) mapResult.put(source,1);
                else mapResult.put(source,mapResult.get(source)+1);
            }
        }

        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        client.clearScroll(clearScrollRequest,RequestOptions.DEFAULT);

        return mapResult;

    }
}
