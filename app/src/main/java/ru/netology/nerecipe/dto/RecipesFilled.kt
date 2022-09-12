package ru.netology.nerecipe.dto

object RecipesFilled {
    private var primId = 0L

    val empty = Recipe(
        id = 0,
        pos = 0,
        author = "Empty",
        name = "Empty",
        category = "Empty",
        content = "",
        likedByMe = false,
    )

    val emptyStage = Stage(
        id = 0,
        pos = 0,
        idRecipe = 0,
        name = "Empty",
        description = "Empty",
        pictureId = 0
    )

    val recipesFilled = listOf(
        Recipe(
            id = primId,
            pos = 1,
            author = "Иванов Иван",
            name = "Рецепты русской кухни",
            category = "Русская",
            content = "Русская кухня — понятие очень ёмкое. Есть старая, традиционная русская кухня с блинами, пирогами, киселями. А есть русская кухня, рецепты которой сформировались значительно позже. В этом смысле пельмени, оливье, бефстроганов — тоже блюда русской кухни.",
            likedByMe = false,
            likes = 999,
            shared = 1_099,
            viewed = 2_390_480,
            video = "https://www.youtube.com/watch?v=dR_yAbTcWVw"
        ),
        Recipe(
            id = primId,
            pos = 2,
            author = "Петров Петр",
            name = "Рецепты средиземноморской кухни",
            category = "Средиземноморская",
            content = "Львиная доля пользы заключается в том, что средиземноморская кухня содержит мало животных жиров, в основном это рыба и морепродукты, свежие овощи, сыр. Важным элементом средиземноморской кухни является оливковое масло. Из этих продуктов в основном и готовятся блюда средиземноморской кухни.",
            likedByMe = false,
            likes = 999,
            shared = 9_996,
            viewed = 2_390_480,
            video = "https://www.youtube.com/watch?v=mgdxKpwkZAQ"
        ),
    )
}