package me.kujio.sprout.system.service.impl;

import lombok.RequiredArgsConstructor;
import me.kujio.sprout.core.service.TableServiceImpl;
import me.kujio.sprout.system.entity.SysDictItem;
import me.kujio.sprout.system.service.SysDictItemService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SysDictItemServiceImpl extends TableServiceImpl<SysDictItem> implements SysDictItemService {

}
