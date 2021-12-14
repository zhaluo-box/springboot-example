import com.example.boot.base.common.service.AbstractRuntimeService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class DefaultApp extends AbstractRuntimeService {

    @Override
    protected String getInfo() {
        return "测试APP";
    }

    @Override
    protected void handleData() {
        System.out.println("处理数据!");
    }

}
