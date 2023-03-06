package ru.modulkassa.payment.library.ui

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.databinding.FragmentPaymentBinding
import ru.modulkassa.payment.library.entity.ErrorType
import ru.modulkassa.payment.library.entity.InventPosition
import ru.modulkassa.payment.library.entity.PaymentOptions
import ru.modulkassa.payment.library.entity.PaymentResultError

internal class PaymentBottomSheetFragment : BottomSheetDialogFragment(), PaymentView {

    companion object {
        const val TAG = "paymentBottomSheetFragment"

        fun create(): PaymentBottomSheetFragment {
            return PaymentBottomSheetFragment()
        }
    }

    private val presenter = PaymentPresenter()

    override fun getTheme(): Int = R.style.Theme_Modul_BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    private var binding: FragmentPaymentBinding? = null

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
        } else {
            setErrorResult(NoPaymentOptionsException())
        }
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setErrorResult(CanceledByUserException())
    }

    override fun setErrorResult(error: Throwable) {
        // todo перенести маппинг в презентер или ExceptionSolver
        val result = when (error) {
            is NoPaymentOptionsException -> {
                PaymentResultError(
                    getString(R.string.error_no_payment_options),
                    ErrorType.INVALID_DATA
                )
            }
            is CanceledByUserException -> {
                PaymentResultError(
                    getString(R.string.error_cancelled_by_user),
                    ErrorType.CANCELLED
                )
            }
            else -> PaymentResultError(error.message ?: getString(R.string.error_unknown), ErrorType.UNKNOWN)
        }

        requireActivity().setResult(Activity.RESULT_CANCELED, Intent().apply { putExtras(result.toBundle()) })
        requireActivity().finish()
    }

    override fun showDescription(description: String) {
        binding?.description?.text = description
    }

    override fun showPositions(positions: List<InventPosition>) {
        // todo SDK-6 Отобразить список позиций для оплаты
    }
}