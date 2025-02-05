import React, { useCallback, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { selectCartItems } from "../../store/features/cart";
import NumberInput from "../../components/NumberInput/NumberInput";
import {
  deleteItemFromCartAction,
  updateItemToCartAction,
} from "../../store/actions/cartAction";
import DeleteIcon from "../../components/common/DeleteIcon";
import Modal from "react-modal";
import { customStyles } from "../../styles/modal";

const headers = [
  "Product Details",
  "Price",
  "Quantity",
  "Shipping",
  "Subtotal",
  "Action",
];

const Cart = () => {
  const cartItems = useSelector(selectCartItems);
  const dispatch = useDispatch();
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [deleteItem, setDeleteItem] = useState({});

  const onChangeQuantity = useCallback(
    (value, productId, variantId) => {
      console.log("Received ", value);
      dispatch(
        updateItemToCartAction({
          productId: productId,
          variant_id: variantId,
          quantity: value,
        })
      );
    },
    [dispatch]
  );

  const onDeleteProduct = useCallback((productId, variantId) => {
    setModalIsOpen(true);
    setDeleteItem({
      productId: productId,
      variantId: variantId,
    });
  }, []);

  const oncloseModal = useCallback(() => {
    setDeleteItem({});
    setModalIsOpen(false);
  }, []);

  const onDeleteItem = useCallback(() => {
    dispatch(deleteItemFromCartAction(deleteItem));
    setModalIsOpen(false);
    // setDeleteItem({});
  }, [deleteItem, dispatch]);

  return (
    <>
      <div className="p-4">
        <p className="text-sm text-black p-4">Shopping Bag</p>
        {/* 장바구니 */}
        <table className="w-full text-lg">
          <thead className="text-sm bg-black text-white uppercase">
            <tr>
              {headers?.map((header) => {
                return (
                  <th scope="col" className="px-6 py-3">
                    {header}
                  </th>
                );
              })}
            </tr>
          </thead>
          <tbody>
            {cartItems?.map((item, index) => {
              return (
                <tr className="p-4">
                  <td>
                    <div className="flex">
                      <img
                        src={item?.thumbnail}
                        alt={"product " + index}
                        className="w-[120px] h-[120px] object-cover p-4"
                      />
                      <div className="flex flex-col text-sm px-2 text-gray-600">
                        <p>{item?.name || "Name"}</p>
                        <p>Size {item?.variant?.size}</p>
                        <p>Color {item?.variant?.color}</p>
                      </div>
                    </div>
                  </td>
                  <td>
                    <p className="text-center text-sm text-gray-600">
                      ${item?.price}
                    </p>
                  </td>
                  <td>
                    <NumberInput
                      max={2}
                      quantity={item?.quantity}
                      onChangeQuantity={(value) =>
                        onChangeQuantity(
                          value,
                          item?.productId,
                          item?.variant?.id
                        )
                      }
                    />
                  </td>
                  <td>
                    <p className="text-center text-sm text-gray-600">FREE</p>
                  </td>
                  <td>
                    <p className="text-center text-sm text-gray-600">
                      ${item?.subTotal}
                    </p>
                  </td>
                  <td>
                    <button
                      className="flex items-center w-full justify-center"
                      onClick={() =>
                        onDeleteProduct(item?.productId, item?.variant?.id)
                      }
                    >
                      <DeleteIcon />
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
      <Modal
        isOpen={modalIsOpen}
        onRequestClose={oncloseModal}
        style={customStyles}
        contentLabel="Remove Item"
      >
        <p>Are you sure you want to remove this item?</p>
        <div className="flex justify-between p-4">
          <button className="h-[48px]" onClick={oncloseModal}>
            Cancel
          </button>
          <button
            className="bg-black text-white w-[80px] h-[48px] border rounded-lg "
            onClick={onDeleteItem}
          >
            Remove
          </button>
        </div>
      </Modal>
    </>
  );
};

export default Cart;
