package com.dataart.dancestudio.model.dto.view;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FilteredViewListPage<T> extends ViewListPage<T> {

    private final Map<String, String> filerParameters;

}



