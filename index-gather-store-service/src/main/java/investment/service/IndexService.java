package investment.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import investment.pojo.Index;
import investment.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames="indexes")
public class IndexService {
    private List<Index> indexes;
    @Autowired
    RestTemplate restTemplate;

    @CacheEvict(allEntries=true)
    public void remove(){
    }

    @HystrixCommand(fallbackMethod = "third_part_not_connected")
    @Cacheable(key="'all_codes'")
    public List<Index> get(){
        return CollUtil.toList();
    }

    @HystrixCommand(fallbackMethod = "thirdPartyNotConnected")
    @CachePut(key = "'all_codes'",unless = "#result[0].code=='000000'")
    public List<Index> fresh() {
        return fetch_indexes_from_third_part();
    }

    public List<Index> fetch_indexes_from_third_part(){
        List<Map<String,String>> temp= restTemplate.getForObject("http://127.0.0.1:8090/indexes/codes.json",List.class);
        return map2Index(temp);
    }

    private List<Index> map2Index(List<Map<String,String>> temp) {
        List<Index> indexes = new ArrayList<>();
        for (Map<String,String> map : temp) {
            String code = map.get("code");
            String name = map.get("name");
            Index index= new Index();
            index.setCode(code);
            index.setName(name);
            indexes.add(index);
        }
        return indexes;
    }

    public List<Index> third_part_not_connected(){
        Index index= new Index();
        index.setCode("000000");
        index.setName("无效指数代码");
        return CollectionUtil.toList(index);
    }

}