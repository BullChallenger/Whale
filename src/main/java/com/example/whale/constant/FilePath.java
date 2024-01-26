package com.example.whale.constant;

import java.io.File;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilePath {

    ABSOLUTE_PATH(new File("").getAbsolutePath() + File.separator);

    private final String path;

}
