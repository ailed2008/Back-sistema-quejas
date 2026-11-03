package com.banco.app_quejas.dto;

import com.banco.app_quejas.entity.Rol;

public record RegisterRequest(
        String username,
        String password,
        Rol rol
) {

}
