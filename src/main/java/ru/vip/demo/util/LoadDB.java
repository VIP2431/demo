package ru.vip.demo.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.serviceimpl.EstimateImpl;
import ru.vip.demo.type.Category;
import ru.vip.demo.type.Unit;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoadDB {

	private final ItemDirectory itemDirectory = new ItemDirectory();

	@Autowired
	public EstimateImpl repository;

	public void loadItemDirectory(){

    		repository.save(itemDirectory);
		repository.save(ItemDirectory.builder()
                .category(Category.SERVICE)
				.vendor("Поставщик2")
				.code("Код поставщика2")
				.name("Накладные расходы2")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.2))
            .build());
		repository.save(ItemDirectory.builder()
                .category(Category.MATERIAL)
				.vendor("Поставщик3")
				.code("Код поставщика3")
				.name("Накладные расходы3")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.3))
            .build());
		repository.save(ItemDirectory.builder()
                .category(Category.TOOLS)
				.vendor("Поставщик4")
				.code("Код поставщика4")
				.name("Накладные расходы4")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.4))
            .build());
		repository.save(ItemDirectory.builder()
                .category(Category.NOT_CATEGORY)
				.vendor("Поставщик5")
				.code("Код поставщика5")
				.name("Накладные расходы5")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.5))
            .build());
		repository.save(ItemDirectory.builder()
                .category(Category.WORK)
				.vendor("Поставщик6")
				.code("Код поставщика6")
				.name("Накладные расходы6")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.6))
            .build());
		repository.save(ItemDirectory.builder()
                .category(Category.MATERIAL)
				.vendor("Поставщик7")
				.code("Код поставщика7")
				.name("Накладные расходы7")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.7))
            .build());
		repository.save(ItemDirectory.builder()
                .category(Category.WORK)
				.vendor("Поставщик8")
				.code("Код поставщика8")
				.name("Накладные расходы8")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.8))
            .build());
		repository.save(ItemDirectory.builder()
                .category(Category.MATERIAL)
				.vendor("Поставщик9")
				.code("Код поставщика9")
				.name("Накладные расходы9")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.9))
            .build());
		repository.save(ItemDirectory.builder()
                .category(Category.WORK)
				.vendor("Поставщик10")
				.code("Код поставщика10")
				.name("Накладные расходы10")
				.unit(Unit.STEP)
				.price(BigDecimal.valueOf(1.0))
            .build());
		}

}
