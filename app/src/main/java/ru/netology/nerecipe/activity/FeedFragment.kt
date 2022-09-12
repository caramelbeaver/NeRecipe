package ru.netology.nerecipe.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.RecipeCardFragment.Companion.idArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.idPosArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.idSubArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.authorArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.nameArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.catArg
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.textArg
import ru.netology.nerecipe.adapter.CategoryAdapter
import ru.netology.nerecipe.adapter.OnInteractionCatListener
import ru.netology.nerecipe.adapter.OnInteractionListener
import ru.netology.nerecipe.adapter.RecipesAdapter
import ru.netology.nerecipe.databinding.FragmentFeedBinding
import ru.netology.nerecipe.dto.Category
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.util.AndroidUtils
import ru.netology.nerecipe.viewmodel.RecipeViewModel


class FeedFragment : Fragment() {
    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val categoryAdapter = CategoryAdapter(object : OnInteractionCatListener {
            override fun onClicked(category: Category) {
                if (category.selected && category.titleRu != "Все категории")
                    viewModel.getByFilterOnCat(category.titleRu.trim())
                else
                    viewModel.getByFilter("", "", "", false)
            }
        })
        binding.recipeCategory.adapter = categoryAdapter

        binding.apply {
            with(binding) {
                searchText.setOnClickListener {
                    if (recipeSearchText.text.isNullOrEmpty()) {
                        viewModel.getByFilterOnName("%")
                    } else {
                        viewModel.getByFilterOnName(recipeSearchText.text.toString().trim())
                    }
                    AndroidUtils.hideKeyboard(requireView())
                    footerList.isChecked = false
                    footerMyRecipes.isChecked = false
                    footerFavorite.isChecked = false
                }
                clearText.setOnClickListener {
                    AndroidUtils.hideKeyboard(requireView())
                    recipeSearchText.setText("")
                    viewModel.getByFilter("", "", "", false)
                }
            }

            with(footerList) {
                val states = intArrayOf(android.R.attr.state_enabled)
                val defaultTextColor = textColors.defaultColor

                setOnClickListener {
                    isChecked = true
                    iconTint =
                        ColorStateList(arrayOf(states), intArrayOf(Color.BLUE)) // "#FF303F9F"
                    setTextColor(Color.BLUE)

                    viewModel.getByFilter("", "", "", false)

                    footerMyRecipes.isChecked = false
                    footerMyRecipes.iconTint =
                        ColorStateList(arrayOf(states), intArrayOf(Color.DKGRAY))
                    footerMyRecipes.setTextColor(defaultTextColor)

                    footerFavorite.isChecked = false
                    footerFavorite.iconTint =
                        ColorStateList(arrayOf(states), intArrayOf(Color.DKGRAY))
                    footerFavorite.setTextColor(defaultTextColor)
                }
            }

            with(footerMyRecipes) {
                val states = intArrayOf(android.R.attr.state_enabled)
                val defaultTextColor = textColors.defaultColor

                setOnClickListener {
                    isChecked = true
                    iconTint = ColorStateList(arrayOf(states), intArrayOf(Color.BLUE))
                    setTextColor(Color.BLUE)

                    viewModel.getByFilter("", "", "", false)
                    viewModel.getByFilterOnAuthor("Me")

                    footerList.isChecked = false
                    footerList.iconTint = ColorStateList(arrayOf(states), intArrayOf(Color.DKGRAY))
                    footerList.setTextColor(defaultTextColor)

                    footerFavorite.isChecked = false
                    footerFavorite.iconTint =
                        ColorStateList(arrayOf(states), intArrayOf(Color.DKGRAY))
                    footerFavorite.setTextColor(defaultTextColor)
                }
            }

            with(footerFavorite) {
                val states = intArrayOf(android.R.attr.state_enabled)
                val defaultTextColor = textColors.defaultColor

                setOnClickListener {
                    isChecked = true
                    iconTint = ColorStateList(arrayOf(states), intArrayOf(Color.BLUE))
                    setTextColor(Color.BLUE)

                    viewModel.getByFilter("", "", "", false)
                    viewModel.getByFilterOnLike(true)

                    footerList.isChecked = false
                    footerList.iconTint = ColorStateList(arrayOf(states), intArrayOf(Color.DKGRAY))
                    footerList.setTextColor(defaultTextColor)

                    footerMyRecipes.isChecked = false
                    footerMyRecipes.iconTint =
                        ColorStateList(arrayOf(states), intArrayOf(Color.DKGRAY))
                    footerMyRecipes.setTextColor(defaultTextColor)
                }
            }
        }

        val adapter = RecipesAdapter(object : OnInteractionListener {
            private fun toNewRecipeFragment(recipe: Recipe) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_newRecipeFragment,
                    Bundle().apply {
                        idSubArg = recipe.id.toString()
                        authorArg = recipe.author
                        nameArg = recipe.name
                        catArg = recipe.category
                        textArg = recipe.content
                    }
                )
            }

            private fun toRecipeCardFragment(recipe: Recipe) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_recipeCardFragment,
                    Bundle().apply {
                        idArg = recipe.id.toString()
                    }
                )
            }

            override fun onAuthor(recipe: Recipe) {
                toRecipeCardFragment(recipe)
            }

            override fun onName(recipe: Recipe) {
                toRecipeCardFragment(recipe)
            }

            override fun onCategory(recipe: Recipe) {
                toRecipeCardFragment(recipe)
            }

            override fun onContent(recipe: Recipe) {
                toRecipeCardFragment(recipe)
            }

            override fun onEdit(recipe: Recipe) {
                viewModel.edit(recipe)
                viewModel.updateStages()
                toNewRecipeFragment(recipe)
            }

            override fun onLike(recipe: Recipe) {
                viewModel.likeById(recipe.id)
            }

            override fun onRemove(recipe: Recipe) {
                viewModel.removeById(recipe.id)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNotEmpty()) {
                adapter.submitList(recipes)

                binding.root.findViewById<TextView>(R.id.empty_view_recipe).visibility =
                    View.INVISIBLE
                binding.root.findViewById<RecyclerView>(R.id.list).visibility = View.VISIBLE
            } else {
                binding.root.findViewById<TextView>(R.id.empty_view_recipe).visibility =
                    View.VISIBLE
                binding.root.findViewById<RecyclerView>(R.id.list).visibility = View.INVISIBLE
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(
                R.id.action_feedFragment_to_newRecipeFragment,
                Bundle().apply {

                    val recipesCount = viewModel.data.value
                    idPosArg =
                        if (!recipesCount.isNullOrEmpty())
                            (recipesCount.maxOf { it.pos } + 1).toString()
                        else 1.toString()

                    idSubArg = null
                    authorArg = null
                    nameArg = null
                    catArg = null
                    textArg = null
                }
            )
        }
        return binding.root
    }
}