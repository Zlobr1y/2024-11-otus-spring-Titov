package ru.otus.hw.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class TestFileNameProviderImpl implements TestFileNameProvider {

    String testFileName;
}
