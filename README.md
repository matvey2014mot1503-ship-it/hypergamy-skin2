# Hypergamy Skin

Клиентский Fabric-мод для Minecraft **1.21.4**. Вместо обычной объёмной модели каждый игрок
рисуется плоской панелью с картинкой `panel.png`. Над панелью остаётся имя игрока.
Другие игроки ничего не замечают, мод работает только у вас.

## Управление
Клавиша **K** (меняется в «Управление» → «Hypergamy Skin») включает и выключает панель.

## Настройки
Если установлен Mod Menu, в нём у мода есть кнопка настроек с переключателем ВКЛ/ВЫКЛ. Состояние сохраняется в `config/hypergamy.properties`.

## Установка
1. Fabric Loader для 1.21.4: https://fabricmc.net/use
2. Fabric API для 1.21.4
3. (по желанию) Mod Menu для 1.21.4 — для экрана настроек
4. Файл `hypergamy-skin-*.jar` из вкладки Releases положить в `.minecraft/mods`

## Своя картинка
Замените `src/main/resources/assets/hypergamy/textures/panel.png` и закоммитьте.
Размеры панели в блоках лежат в `PlayerPanelMixin.java` (`PANEL_WIDTH`, `PANEL_HEIGHT`).
