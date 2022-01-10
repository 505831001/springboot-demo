package com.coder.springsecurity.utils;

import com.coder.springsecurity.dto.PermissionDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
public class TreeUtil {
    //todo 判断list是否为空
    public static List<PermissionDto> tree(List<PermissionDto> listByRoleId, List<PermissionDto> permissionDtos ){
        // if (listByRoleId == null & listByRoleId.size() ==0){
        //     throw
        // }
        List<Integer> collect = listByRoleId.stream().map(PermissionDto::getId).collect(Collectors.toList());
        List<Integer> collect1 = permissionDtos.stream().map(PermissionDto::getId).collect(Collectors.toList());
        for (Integer item : collect) {// 遍历list2
            if (collect1.contains(item)) {// 如果存在这个数
                PermissionDto permissionDto = new PermissionDto();
                permissionDto = permissionDtos.get(item-1);
                permissionDto.setCheckArr("1");
                permissionDtos.set(item-1,permissionDto);
            }
        }
        return permissionDtos;
    }
}
