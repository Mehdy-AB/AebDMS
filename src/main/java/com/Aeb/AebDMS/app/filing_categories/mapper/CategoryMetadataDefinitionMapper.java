package com.Aeb.AebDMS.app.filing_categories.mapper;

import com.Aeb.AebDMS.app.advicer.exceptions.BaseException;
import com.Aeb.AebDMS.app.filing_categories.dto.CategoryMetadataDefinitionDto;
import com.Aeb.AebDMS.app.filing_categories.dto.req.MetaDataListReq;
import com.Aeb.AebDMS.app.filing_categories.model.*;
import com.Aeb.AebDMS.app.filing_categories.repository.MetaDataListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMetadataDefinitionMapper {
    private final MetaDataListRepository metaDataListRepository;

//    public CategoryMetadataDefinition toEntity(CategoryMetadataDefinitionDto dto){
//        if(dto.getDataType().equals(MetadataType.LIST)) {
//            ListMetaData listMetaData;
//            if(dto.getListId() != null){
//                listMetaData = metaDataListRepository.findById(dto.getListId()).orElseThrow(()->
//                        new BaseException("list not found",HttpStatus.NOT_FOUND));
//
//            }else if(dto.getList() != null){
//                listMetaData = ListMetaData.builder()
//                        .name(dto.getList().getName())
//                        .description(dto.getList().getDescription())
//                        .includeOtherValue(dto.getList().isMandatory())
//                        .options(dto.getList().getOption().stream().map(o->
//                                 ListMetadataOption.builder()
//                                         .name(o)
//                                         .build()
//                        ).toList())
//                        .build();
//            }else throw new BaseException("List meta data needs to be specified", HttpStatus.BAD_REQUEST);
//
//            return CategoryMetadataDefinition.builder()
//                    .dataType(dto.getDataType())
//                    .key(dto.getKey())
//                    .mandatory(dto.getMandatory())
//                    .list(listMetaData)
//                    .build();
//        }
//        return CategoryMetadataDefinition.builder()
//                .dataType(dto.getDataType())
//                .key(dto.getKey())
//                .mandatory(dto.getMandatory())
//                .build();
//    }

    public CategoryMetadataDefinition toEntity(CategoryMetadataDefinitionDto dto, FilingCategory category){
        if(dto.getDataType().equals(MetadataType.LIST)) {
            ListMetaData listMetaData;
            if(dto.getListId() != null){
                listMetaData = metaDataListRepository.findById(dto.getListId()).orElseThrow(()->
                        new BaseException("list not found",HttpStatus.NOT_FOUND));

            }else if(dto.getList() != null){
                listMetaData = ListMetaData.builder()
                        .name(dto.getList().getName())
                        .description(dto.getList().getDescription())
                        .includeOtherValue(dto.getList().isMandatory())
                        .build();

                dto.getList().getOption().forEach(o -> {
                    listMetaData.addOption(ListMetadataOption.builder()
                            .name(o)
                            .build());
                });

            }else throw new BaseException("List meta data needs to be specified", HttpStatus.BAD_REQUEST);

            return CategoryMetadataDefinition.builder()
                    .dataType(dto.getDataType())
                    .key(dto.getKey())
                    .category(category)
                    .mandatory(dto.getMandatory())
                    .list(listMetaData)
                    .build();
        }
        return CategoryMetadataDefinition.builder()
                .dataType(dto.getDataType())
                .key(dto.getKey())
                .category(category)
                .mandatory(dto.getMandatory())
                .build();
    }

    public CategoryMetadataDefinitionDto toDto(CategoryMetadataDefinition entity){
        ListMetaData listMetaData = entity.getList();

        return CategoryMetadataDefinitionDto.builder()
                .id(entity.getId().toString())
                .listId(listMetaData!=null?listMetaData.getId():null)
                .list(listMetaData!=null?MetaDataListReq.builder()
                        .name(listMetaData.getName())
                        .description(listMetaData.getDescription())
                        .option(listMetaData.getOptions().stream().map(ListMetadataOption::getName).collect(Collectors.toList()))
                        .build():null)
                .dataType(entity.getDataType())
                .key(entity.getKey())
                .mandatory(entity.isMandatory())
                .build();
    }

//    List<CategoryMetadataDefinition> toEntityList(List<CategoryMetadataDefinitionDto> dtoList){
//        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
//    }

    List<CategoryMetadataDefinition> toEntityList(List<CategoryMetadataDefinitionDto> dtoList, FilingCategory category){
        return dtoList.stream().map(one ->toEntity(one,category)).collect(Collectors.toList());
    }

    List<CategoryMetadataDefinitionDto> toDtoList(List<CategoryMetadataDefinition> entityList){
        return  entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

}

