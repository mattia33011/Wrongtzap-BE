package it.mag.wrongtzap.config

import cn.hutool.core.lang.Snowflake
import cn.hutool.core.util.IdUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SnowflakeConfig {

    @Bean
    fun snowFlake(): Snowflake{
        val threadId: Long = Thread.currentThread().id % 32
        val dataCenterID: Long = 1
        return IdUtil.getSnowflake(threadId, dataCenterID)
    }
}