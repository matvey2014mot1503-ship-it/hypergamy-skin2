# Hypergamy Skin

Клиентский Fabric-мод для Minecraft **1.21.4**. Вместо обычной объёмной модели каждый игрок
рисуется плоской панелью с картинкой `panel.png`. Над панелью остаётся имя игрока.
Другие игроки ничего не замечают, мод работает только у вас.

## Управление
Клавиша **K** (меняется в «Управление» → «Hypergamy Skin») включает и выключает панель.

## Установка
1. Fabric Loader для 1.21.4: https://fabricmc.net/use
2. Fabric API для 1.21.4
3. Файл `hypergamy-skin-*.jar` из вкладки Releases положить в `.minecraft/mods`

## Своя картинка
Замените `src/main/resources/assets/hypergamy/textures/panel.png` и закоммитьте.
Размеры панели в блоках лежат в `PlayerPanelMixin.java` (`PANEL_WIDTH`, `PANEL_HEIGHT`).
