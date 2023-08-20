package me.kujio.sprout;

import me.kujio.sprout.system.entity.SysDict;
import me.kujio.sprout.system.entity.SysDictItem;
import me.kujio.sprout.system.service.SysDictService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SysDictServiceTests {
    @Autowired
    private SysDictService sysDictService;

    @Test
    void testDict() {
        sysDictService.dict(List.of("name"));
    }

    @Test
    void testWithItems() {
        SysDict sysDict = new SysDict() {{
            setId(12);
            setName("test1");
            setLabel("测试1");
            setOwner(Owner.USER);
        }};
        List<SysDictItem> dictItems = List.of(
                new SysDictItem() {{
                    setId(10);
                    setName("test1");
                    setLabel("测试1");
                }},
//                new SysDictItem() {{
//                    setId(11);
//                    setName("test11");
//                    setLabel("测试11");
//                }},
                new SysDictItem() {{
                    setId(12);
                    setName("test222");
                    setLabel("测试222");
                }},
                new SysDictItem() {{
                    setName("test1111");
                    setLabel("测试1111");
                }}
        );
        sysDictService.putWithItems(sysDict,dictItems);
    }
}
