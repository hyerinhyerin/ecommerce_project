import React, { useMemo } from "react";
import SvgStarIcon from "../common/SvgStarIcon";
import SvgEmptyStar from "../common/SvgEmptyStar";

const Rating = ({ rating }) => {
  console.log("rating: ", rating);
  const ratingNumber = useMemo(() => {
    return Array(Math.round(Number(rating))).fill();
  }, [rating]);
  return (
    <div className="flex items-center">
      {ratingNumber?.map((index) => (
        <SvgStarIcon key={index} />
      ))}
      {Array(5 - ratingNumber?.length)
        .fill()
        .map((index) => (
          <SvgEmptyStar key={"empty " + index} />
        ))}
      <p className="px-2 text-gray-500">{rating}</p>
    </div>
  );
};

export default Rating;
