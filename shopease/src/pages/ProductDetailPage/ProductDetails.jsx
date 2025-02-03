import React, { useCallback, useEffect, useMemo, useState } from "react";
import { Link, useLoaderData } from "react-router-dom";
import Breadcrumb from "../../components/Breadcrumb/Breadcrumb";
import content from "../../data/content.json";
import Rating from "../../components/Rating/Rating";
import SizeFilter from "../../components/Filters/SizeFilter";
import ProductColors from "./ProductColors";
import CartIcon from "../../components/common/CartIcon";
import SvgCreditCard from "../../components/common/SvgCreditCard";
import SvgCloth from "../../components/common/SvgCloth";
import SvgShipping from "../../components/common/SvgShipping";
import SvgReturn from "../../components/common/SvgReturn";
import SectionHeading from "../../components/Sections/SectionHeading/SectionHeading";
import ProductCard from "../ProductListPage/ProductCard";
import { useDispatch, useSelector } from "react-redux";
import _ from "lodash";
import { getAllProducts } from "../../api/fetchProducts";

// const categories = content?.categories;

const extraSections = [
  {
    icon: <SvgCreditCard />,
    label: "Secure payment",
  },
  {
    icon: <SvgCloth />,
    label: "Size & Fit",
  },
  {
    icon: <SvgShipping />,
    label: "Free Shipping",
  },
  {
    icon: <SvgReturn />,
    label: "Free Shipping & Returns",
  },
];

const ProductDetails = () => {
  const { product } = useLoaderData();
  const [image, setImage] = useState();
  const [breadCrumbLinks, setBreadCrumbLinks] = useState([]);
  const dispatch = useDispatch();
  const cartItems = useSelector((state) => state.cartState?.cart);
  const [similarProducts, setSimilarProducts] = useState([]);
  const categories = useSelector((state) => state?.categoryState?.categories);

  console.log("product: ", product?.categoryId);

  // const similarProducts = useMemo(() => {
  //   return content?.products?.filter(
  //     (item) => item?.type_id === product?.type_id && item?.id !== product?.id
  //   );
  // }, [product]);

  const productCategory = useMemo(() => {
    return categories?.find((category) => category?.id === product?.categoryId);
  }, [product, categories]);

  useEffect(() => {
    getAllProducts(product?.categoryId, product?.categoryTypeId)
      .then((res) => {
        const excludedProduct = res?.filter((item) => item?.id !== product?.id);
        setSimilarProducts(excludedProduct);
      })
      .catch(() => {});
  }, [product?.categoryId, product?.categoryTypeId]);

  useEffect(() => {
    setImage(product?.thumbnail);
    setBreadCrumbLinks([]);
    const arrayLinks = [
      {
        title: "Shop",
        path: "/",
      },
      {
        title: productCategory?.name,
        path: productCategory?.name,
      },
    ];

    const productType = productCategory?.categoryTypes?.find(
      (item) => item?.id === product?.categoryTypeId
    );

    if (productType) {
      arrayLinks?.push({
        title: productType?.name,
        path: productType?.name,
      });
    }
    // const productType = productCategory?.types?.find(
    //   (item) => item?.type_id === product?.type_id
    // );
    // console.log("product type:", productType?.name, productCategory?.name);

    // if (productType) {
    //   arrayLinks?.push({
    //     title: productType?.name,
    //     path: productType?.name,
    //   });
    // }

    setBreadCrumbLinks(arrayLinks);
  }, [productCategory, product]);

  const addItemToCart = useCallback(() => {}, []);

  const colors = useMemo(() => {
    const colorSet = _.uniq(_.map(product?.variants, "color"));
    return colorSet;
  }, [product]);

  const sizes = useMemo(() => {
    const sizeSet = _.uniq(_.map(product?.variants, "size"));
    return sizeSet;
  }, [product]);

  return (
    <>
      <div className="flex flex-col  md:flex-row px-10">
        <div className="w-[100%] lg:w-[50%] md:w-[40%]">
          {/* image */}
          <div className="flex flex-col  md:flex-row">
            <div className="w-[100%] md:w-[30%] justify-center h-[40px] md:h-[420px]">
              <div className="flex flex-row md:flex-col justify-center h-full">
                {product?.productResources?.map((item, index) => (
                  <button
                    onClick={() => setImage(item?.url)}
                    className="rounded-lg w-fit p-2 mb-2"
                  >
                    <img
                      src={item?.url}
                      className="h-[60px] w-[60px] rounded-lg bg-cover bg-center hover:scale-105"
                      alt={"sample-" + index}
                    />
                  </button>
                ))}
              </div>
            </div>
            <div className="w-full md:w-[80%] flex justify-center md:pt-0 pt-10">
              <img
                className="h-[420px] w-full max-h-[520px] border rounded-lg cursor-pointer object-cover"
                src={image}
                alt={product?.name}
              />
            </div>
          </div>
        </div>
        <div className="w-[60%] px-10">
          <Breadcrumb links={breadCrumbLinks} />
          <p className="text-3xl pt-4">{product?.name}</p>
          <Rating rating={product?.rating} />
          {/* price tag */}
          <p className="text-xl bold py-2">${product?.price}</p>
          <div className="flex flex-col">
            <div className="flex gap-2">
              <p className="text-sm bold">Select Size</p>
              <Link
                className="text-sm text-gray-500 hover:text-gray-900"
                to={"https://en.wikipedia.org/wiki/Clothing_sizes"}
                target="_blank"
              >
                {"Size Guide ->"}
              </Link>
            </div>
          </div>
          <div className="mt-2">
            <SizeFilter sizes={sizes} hidleTitle multi={false} />
          </div>
          <div>
            <p className="text-lg bold">Colors Available</p>
            <ProductColors colors={colors} />
          </div>
          <div className="flex py-4">
            <button className="bg-black border rounded-lg w-[155px] px-2">
              <div className="flex items-center rounded-lg bg-black text-white ">
                <CartIcon bgColor={"black"} /> Add to cart
              </div>
            </button>
          </div>
          <div className="grid grid-cols-2 gap-4 pt-4">
            {/* extraSections */}
            {extraSections?.map((section) => (
              <div className="flex items-center">
                {section?.icon}
                <p className="px-2">{section?.label}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
      {/* product description */}
      <SectionHeading title={"Product Description"} />
      <div className="md:w-[50%] w-full p-2">
        <p className="px-8">{product?.description}</p>
      </div>

      <SectionHeading title={"Similar Products"} />
      <div className="flex px-10">
        <div className="pt-4 grid grid-cols-1 lg:grid-cols-4 md:grid-cols-3 gap-8 px-2 pb-10">
          {similarProducts?.map((item, index) => {
            return <ProductCard key={index} {...item} />;
          })}
          {!similarProducts?.length && <p>No Products Found!</p>}
        </div>
      </div>
    </>
  );
};

export default ProductDetails;
