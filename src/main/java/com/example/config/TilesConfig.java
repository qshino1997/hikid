package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

@Configuration
public class TilesConfig {
    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer cfg = new TilesConfigurer();
        cfg.setDefinitions("/WEB-INF/tiles/tiles-defs.xml");
        return cfg;
    }

    @Bean
    public TilesViewResolver tilesViewResolver() {
        return new TilesViewResolver();
    }
}
