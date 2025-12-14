package com.kit.projectdesign.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.kit.projectdesign.R
import com.kit.projectdesign.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RecipeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = args.recipe

        binding.detailRecipeImage.load(recipe.imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_recipe)
            error(R.drawable.ic_recipe)
        }
        binding.detailRecipeName.text = recipe.name
        binding.detailRecipeServings.text = "(${recipe.servings})"

        // 材料と調味料をフォーマットして表示
        binding.detailRecipeIngredients.text = recipe.ingredients.joinToString("\n")
        binding.detailRecipeSeasonings.text = recipe.seasonings.joinToString("\n")

        // 作り方をRecyclerViewにセット
        val instructionAdapter = InstructionAdapter(recipe.instructions)
        binding.recyclerViewInstructions.adapter = instructionAdapter

        // コツ・ポイントをアスタリスク付きでフォーマットして表示
        binding.detailRecipeTips.text = recipe.tips.map { tip ->
            "* $tip"
        }.joinToString("\n")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
