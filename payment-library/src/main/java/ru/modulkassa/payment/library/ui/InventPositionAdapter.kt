package ru.modulkassa.payment.library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.modulkassa.payment.library.databinding.ItemInventPositionBinding
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.network.BigDecimalFormatter
import java.math.RoundingMode

internal class InventPositionAdapter(
    private val positions: List<Position>
) : RecyclerView.Adapter<InventPositionAdapter.InventPositionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventPositionViewHolder {
        val binding = ItemInventPositionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return InventPositionViewHolder(binding)
    }

    override fun getItemCount() = positions.size

    override fun onBindViewHolder(holder: InventPositionViewHolder, position: Int) {
        holder.bindView(positions[position])
    }

    inner class InventPositionViewHolder(
        val binding: ItemInventPositionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(position: Position) {
            binding.name.text = position.name
            binding.price.text = RubSuffixSumFormatter().format(position.price)
            binding.quantity.text = BigDecimalFormatter.format(position.quantity, scale = 3)
        }
    }
}
