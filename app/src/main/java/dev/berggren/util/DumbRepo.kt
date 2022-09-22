package dev.berggren.util

import android.util.Size
import dev.berggren.pascalCase
import dev.berggren.ui.*


private val sections = listOf(
    SectionModel(
        name = "Trainers", items = listOf(
            Trainer(id = "", name = "Mohamed", imageUrl = ""),
            Trainer(id = "", name = "Peter", imageUrl = ""),
            Trainer(id = "", name = "Ujwal", imageUrl = ""),
            Trainer(id = "", name = "Ellis", imageUrl = ""),
            Trainer(id = "", name = "Josh", imageUrl = ""),
        ), size = Size(270, 205)
    ),
    SectionModel(
        name = "Programmes", items = listOf(
            Programme(id = "", name = "Get younger", imageUrl = ""),
            Programme(id = "", name = "Get Bilingual", imageUrl = ""),
            Programme(id = "", name = "Get Stronger", imageUrl = ""),
        ), size = Size(484, 440)
    ),
    SectionModel(
        name = "Workout Category",
        items = listOf(
            Category(name = "Balance", imageUrl = categoryImageLink("balance", ".jpg")),
            Category(name = "Strength", imageUrl = categoryImageLink("strength")),
            Category(name = "Cardio", imageUrl = categoryImageLink("cardio", ".jpg")),
        ),
        size = Size(484, 534)
    ),
    SectionModel(
        name = "Practice Category",
        items = listOf("LOWER_BODY", "CORE", "UPPER_BODY", "TOTAL_BODY")
            .map {
                val title = it.pascalCase().replace("_", " ")
                Category(title, categoryImageLink(it.lowercase()))
            },
        size = Size(484, 534)
    ),
)

val sectionMap: List<SectionModel> = sections
