package com.beatshadow.mall.order.feign;

import com.beatshadow.common.utils.R;
import com.beatshadow.mall.order.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/21 02:45
 */
@FeignClient(value = "mall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasstock")
    public  R getSkuHasStock (@RequestBody List<Long> skuIds);

    /**
     * 获取运费信息
     * fare
     */
    @GetMapping("ware/wareinfo/fare")
    public R getFare(@RequestParam("addrId") Long addrId);

    /**
     * 锁定库存
     */
    @PostMapping("/ware/waresku/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVo wareSkuLockVo);

}
