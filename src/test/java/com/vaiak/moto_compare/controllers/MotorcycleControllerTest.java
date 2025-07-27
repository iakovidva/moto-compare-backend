package com.vaiak.moto_compare.controllers;

import static com.vaiak.moto_compare.utils.MotorcycleTestDataFactory.createAdventureDTO;
import static com.vaiak.moto_compare.utils.MotorcycleTestDataFactory.createDetailsDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.enums.Manufacturer;
import com.vaiak.moto_compare.exceptions.MotorcycleNotFoundException;
import com.vaiak.moto_compare.services.MotorcycleService;
import java.util.List;

import com.vaiak.moto_compare.services.UserFavoriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MotorcycleController.class)
@WithMockUser // adds a dummy authenticated user
class MotorcycleControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private MotorcycleService motorcycleService;
    @MockBean private UserFavoriteService userFavoriteService;

    @Test
    void getAllTest() throws Exception {
        List<MotorcycleSummaryDTO> result = List.of(
                createAdventureDTO(1L, Manufacturer.HONDA, "2021"),
                createAdventureDTO(2L, Manufacturer.YAMAHA, "2019-2023"));

        when(motorcycleService.getAllMotorcycles(any())).thenReturn(new PageImpl<>(result));

        mockMvc.perform(get("/api/motorcycles?page=0&size=3"))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        header().string("X-Total-Pages", "1"),
                        header().string("X-Total-Elements", "2"),
                        jsonPath("content").isArray(),
                        jsonPath("content.length()").value(2),
                        jsonPath("page.size").value(2),
                        jsonPath("page.totalElements").value(2),
                        jsonPath("page.totalPages").value(1),
                        jsonPath("content[0].manufacturer").value("HONDA"),
                        jsonPath("content[1].manufacturer").value("YAMAHA")
                ).andDo((r -> System.out.println(r.getResponse().getContentAsString())));

    }

    @Test
    void getAllEmptyTest() throws Exception {
        when(motorcycleService.getAllMotorcycles(any())).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/motorcycles?page=0&size=3"))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        header().string("X-Total-Pages", "1"),
                        header().string("X-Total-Elements", "0"),
                        jsonPath("page.size").value(0),
                        jsonPath("page.totalElements").value(0),
                        jsonPath("page.totalPages").value(1)
                ).andDo((result -> System.out.println(result.getResponse().getContentAsString())));
    }

    @Test
    void getMotorcycleById() throws Exception {
        when(motorcycleService.getMotorcycleDetailsById(1L))
                .thenReturn(createDetailsDTO(1L, Manufacturer.HONDA, Category.ADVENTURE));

        mockMvc.perform(get("/api/motorcycles/1"))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("manufacturer").value("HONDA"),
                        jsonPath("category").value("ADVENTURE"),
                        jsonPath("horsePower").value("95")
                ).andDo( r -> System.out.println(r.getResponse().getContentAsString()));
    }

    @Test
    void getNonExistingMotorcycleById() throws Exception {
        when(motorcycleService.getMotorcycleDetailsById(1L))
                .thenThrow(new MotorcycleNotFoundException("1"));

        mockMvc.perform(get("/api/motorcycles/1"))
                .andExpectAll(
                        status().is(HttpStatus.NOT_FOUND.value()),
                        jsonPath("code").value("MOTORCYCLE_NOT_FOUND"),
                        jsonPath("message").value("Motorcycles with manufacturer: 1 not found")
                ).andDo( r -> System.out.println(r.getResponse().getContentAsString()));
    }


}