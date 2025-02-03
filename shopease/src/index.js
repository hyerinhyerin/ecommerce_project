import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import "react-multi-carousel/lib/styles.css";
import { RouterProvider } from "react-router-dom";
import { router } from "./routes";
import { Provider } from "react-redux";
import store from "./store/store";
import ShopApplicationWrapper from "./pages/ShopApplicationWrapper";
import Shop from "./Shop";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <Provider store={store}>
    <RouterProvider router={router}>
      <Shop />
    </RouterProvider>
  </Provider>
);
