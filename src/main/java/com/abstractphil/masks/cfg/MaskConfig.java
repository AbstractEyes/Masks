package com.abstractphil.masks.cfg;

import lombok.Data;

import java.util.Map;

@Data
public class MaskConfig {
	private Map<String, MaskData> masks;
}
