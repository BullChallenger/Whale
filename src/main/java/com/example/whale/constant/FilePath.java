package com.example.whale.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Getter
@RequiredArgsConstructor
public enum FilePath {

    ABSOLUTE_PATH(new File("").getAbsolutePath() + File.separator + File.separator);

    private final String path;

}
