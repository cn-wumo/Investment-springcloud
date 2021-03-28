package investment.service;

import investment.client.IndexDataClient;
import investment.pojo.IndexData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BackTestService {
    @Qualifier("investment.client.IndexDataClient")
    @Autowired
    IndexDataClient indexDataClient;

    public List<IndexData> listIndexData(String code){
        List<IndexData> result = indexDataClient.getIndexData(code);
        Collections.reverse(result);
        return result;
    }
}