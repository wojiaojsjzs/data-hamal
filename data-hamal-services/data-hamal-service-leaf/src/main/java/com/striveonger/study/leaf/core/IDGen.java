package com.striveonger.study.leaf.core;


import com.striveonger.study.leaf.core.common.Result;

public interface IDGen {
    Result get(String key);

    boolean init();

    static Builder builder() {
        return new Builder();
    }

    class Builder {
        public SnowflakeBuilder snowflake() {
            return new SnowflakeBuilder();
        }

        public SegmentBuilder segment() {
            return new SegmentBuilder();
        }
    }

    class SnowflakeBuilder {

    }

    class SegmentBuilder {

    }
}
