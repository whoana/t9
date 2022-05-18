import React from "react";
import styled from "styled-components";
import "@progress/kendo-theme-default/dist/all.css";
import { Button } from "@progress/kendo-react-buttons";
import { Grid, GridColumn } from "@progress/kendo-react-grid";
import products from "./products.json";
import axios from "axios";
const initialDataState = {
  skip: 0,
  take: 10,
};
const Title = styled.div`
  margin: 20px;
  padding: 10px;
`;
function App() {
  const handleSearch = () => {
    console.log("I'm button");

    const options = {
      url: 'http://127.0.0.1:8090/trace/services/tlogs?method=GET',
      method: 'POST',
      header: {
        'Accept': 'application/json',
        'Content-Type': 'application/json; charset=utf-8',
      },
      data: {
        objectType: 'ComMessage',
        requestObject: {
          fromDate: '20220513000000',
          toDate: '20220513235959',
          integrationId: 'TEST004',
          offset: 20,
          rowCount: 10,
        },
        startTime: '20190416105001001',
        endTime: null,
        errorCd: '0',
        errorMsg: '',
        userId: 'iip',
        appId: 'SU0810R01',
        checkSession: false,
      },
    };
    
    console.log(options);

    axios(options)
      .then((response) => {
        console.log(response);
      })
      .catch((e) => {
        console.log(e);
      });
  };

  const [page, setPage] = React.useState(initialDataState);

  const pageChange = (event) => {
    setPage(event.page);
  };

  return (
    <div>
      <Title>
        <Button themeColor={"primary"} onClick={handleSearch}>
          Search
        </Button>
      </Title>
      <div>
        <Grid
          style={{
            height: "400px",
          }}
          data={products.slice(page.skip, page.take + page.skip)}
          skip={page.skip}
          take={page.take}
          total={products.length}
          pageable={true}
          onPageChange={pageChange}
        >
          <GridColumn field="ProductID" />
          <GridColumn field="ProductName" title="Product Name" />
          <GridColumn field="UnitPrice" title="Unit Price" />
        </Grid>
      </div>
    </div>
  );
}

export default App;
