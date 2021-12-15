import com.example.boot.base.common.service.AbstractRuntimeService;
import com.example.boot.base.common.view.UserView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Slf4j
@Service
public class DefaultApp extends AbstractRuntimeService {

    @Autowired
    private UserView userView;

    @Override
    public String getInfo() {
        return "测试APP";
    }

    @Override
    public void handleData() {
        log.info("处理数据!");

        userView.findAllByUsernameLike("王").forEach(x -> {
            log.info("=========  : {}", x);
        });

    }

}
