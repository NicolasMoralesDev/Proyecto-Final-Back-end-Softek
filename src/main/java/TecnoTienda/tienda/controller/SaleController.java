package TecnoTienda.tienda.controller;


import TecnoTienda.tienda.dto.SaleDTO;
import TecnoTienda.tienda.entity.Item;
import TecnoTienda.tienda.entity.Sale;
import TecnoTienda.tienda.entity.User;
import TecnoTienda.tienda.service.SaleService;
import TecnoTienda.tienda.service.ProductService;
import TecnoTienda.tienda.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class SaleController {

    @Autowired
    SaleService saleService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;


    @Operation(summary = "Endpoint de acceso Rol Usuario, Guarda una orden ")
    @PostMapping("/sale/save")
    public ResponseEntity<?> saveSale(@RequestBody SaleDTO saleDto){
        try {
            User user = userService.findById(saleDto.getIdUser());
            Sale sale = new Sale();
            for (Item item : saleDto.getItemList()) {
                Item i = new Item();
                i.setProduct(productService.findById(item.getProduct().getId()).get());
                i.setAmount(item.getAmount());
                i.setSale(sale);
                sale.getItemList().add(i);
            }
            sale.setAddress(saleDto.getAddress());
            sale.setPhone(saleDto.getPhone());
            sale.setUser(user);
            saleService.saveSale(sale);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Endpoint para traer las ventas de un Usuario")
    @GetMapping("/sale/{id}")
    public ResponseEntity<SaleDTO> getSaleByUserId(@PathVariable("id") int id){
        SaleDTO saleDto = saleService.saleByUserId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(saleDto);
    }
}
