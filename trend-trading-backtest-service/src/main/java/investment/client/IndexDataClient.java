package investment.client;

import investment.pojo.IndexData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "INDEX-DATA-SERVICE",fallback = IndexDataClientFeignHystrix.class)
public interface IndexDataClient {
    @GetMapping("/data/{code}")
    List<IndexData> getIndexData(@PathVariable("code") String code);
}