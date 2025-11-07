package com.kit.projectdesign.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kit.projectdesign.data.InstructionStep
import com.kit.projectdesign.databinding.ListItemInstructionBinding

class InstructionAdapter(
    private val instructions: List<InstructionStep>
) : RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val binding = ListItemInstructionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InstructionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        holder.bind(instructions[position], position + 1)
    }

    override fun getItemCount() = instructions.size

    inner class InstructionViewHolder(private val binding: ListItemInstructionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(step: InstructionStep, stepNumber: Int) {
            binding.instructionText.text = "$stepNumber. ${step.description}"
            binding.instructionImage.setImageResource(step.imageResId)
        }
    }
}
