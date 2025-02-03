import React from "react";

const Categories = ({ types }) => {
  return (
    <div>
      {types?.map((type) => {
        return (
          <div className="flex items-center p-1">
            <input
              type="checkbox"
              className="border rounded-xl w-4 h-4 accent-black text-black"
              name={type?.code}
            />
            <label htmlFor={type?.code} className="px-2 text-[14px]">
              {type?.name}
            </label>
          </div>
        );
      })}
    </div>
  );
};

export default Categories;
