package com.Aeb.AebDMS.app.filing_categories.mapper;

import com.Aeb.AebDMS.app.filing_categories.dto.req.FilingCategoryRequestDto;
import com.Aeb.AebDMS.app.filing_categories.dto.req.MetaDataListReq;
import com.Aeb.AebDMS.app.filing_categories.dto.res.FilingCategoryResponseDto;
import com.Aeb.AebDMS.app.filing_categories.dto.res.MetaDataListRes;
import com.Aeb.AebDMS.app.filing_categories.model.FilingCategory;
import com.Aeb.AebDMS.app.filing_categories.model.ListMetaData;
import com.Aeb.AebDMS.app.filing_categories.model.ListMetadataOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListMapper {

    public ListMetaData toEntity(MetaDataListReq list){
        ListMetaData listMetaData = ListMetaData.builder()
                .id(list.getId())
                .name(list.getName())
                .description(list.getDescription())
                .includeOtherValue(list.isMandatory())
                .build();
        list.getOption().forEach(o->
                 listMetaData.addOption(ListMetadataOption.builder().name(o).build())
                );
        return listMetaData;
    }

    public MetaDataListRes toDto(ListMetaData list){

        return MetaDataListRes.builder()
                .id(list.getId())
                .name(list.getName())
                .description(list.getDescription())
                .mandatory(list.isIncludeOtherValue())
                .option(list.getOptions().stream().map(ListMetadataOption::getName).toList())
                .build();
    }

}
