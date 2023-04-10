package ru.modulkassa.payment.library.ui

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.databinding.FragmentPaymentBinding
import ru.modulkassa.payment.library.domain.PaymentTerminalImpl
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.network.GsonFactory
import ru.modulkassa.payment.library.network.NetworkModule
import java.math.BigDecimal

internal class PaymentBottomSheetFragment : BottomSheetDialogFragment(), PaymentView {

    companion object {
        const val TAG = "paymentBottomSheetFragment"

        fun create(): PaymentBottomSheetFragment {
            return PaymentBottomSheetFragment()
        }
    }

    private val presenter = PaymentPresenter(
        PaymentTerminalImpl(
            api = NetworkModule.payApi,
            gson = GsonFactory.provide()
        )
    )

    private var binding: FragmentPaymentBinding? = null

    private lateinit var inventPositionAdapter: InventPositionAdapter

    override fun getTheme(): Int = R.style.Theme_Modul_BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)

        val options = requireActivity().intent.extras?.let { PaymentOptions.fromBundle(it) }
        if (options != null) {
            presenter.checkPaymentOptionsAndShow(options)

            binding?.payBySbp?.setOnClickListener {
                presenter.payBySbp(options)
            }
            binding?.retry?.setOnClickListener {
                presenter.payBySbp(options)
            }
        } else {
            setErrorResult(NoPaymentOptionsErrorResult())
        }
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
        binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setErrorResult(CanceledByUserErrorResult())
    }

    override fun setErrorResult(error: BaseErrorResult) {
        val result = error.toPaymentResultError(requireContext())
        requireActivity().setResult(Activity.RESULT_CANCELED, Intent().apply { putExtras(result.toBundle()) })
        requireActivity().finish()
    }

    override fun showDescription(description: String) {
        binding?.description?.text = description
    }

    override fun showPositions(positions: List<Position>) {
        binding?.positions?.visibility = View.VISIBLE
        binding?.positions?.layoutManager = LinearLayoutManager(context)
        binding?.positions?.addItemDecoration(
            RecyclerViewMaterialDivider(requireContext(), LinearLayoutManager.VERTICAL)
        )
        inventPositionAdapter = InventPositionAdapter(positions)
        binding?.positions?.adapter = inventPositionAdapter

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun showSum(sum: BigDecimal) {
        binding?.summary?.text = RubSuffixSumFormatter().format(sum)
    }

    override fun showProgress() {
        binding?.descriptionLayout?.visibility = View.INVISIBLE
        binding?.errorLayout?.visibility = View.GONE
        binding?.progressLayout?.visibility = View.VISIBLE
        binding?.progressTitle?.text = getString(R.string.create_payment_progress)
    }

    override fun hideProgress() {
        binding?.progressLayout?.visibility = View.GONE
    }

    override fun sendSbpLink(sbpLink: String) {
        // todo SDK-15 Пробрасывать интент с СБП ссылкой
        Toast.makeText(context, sbpLink, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorScreen() {
        binding?.progressLayout?.visibility = View.GONE
        binding?.errorLayout?.visibility = View.VISIBLE
    }
}