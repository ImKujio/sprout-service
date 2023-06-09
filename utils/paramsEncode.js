function paramsEncode(obj, prefix = null) {
    const str = [];
    for (let key in obj) {
        if (obj.hasOwnProperty(key)) {
            const value = obj[key];
            const finalKey = prefix ? `${prefix}[${key}]` : key;
            if (value !== null && Array.isArray(value)) {
                const encodedKey = encodeURIComponent(finalKey);
                const encodedValues = value.map((val) => encodeURIComponent(val)).join(",");
                str.push(`${encodedKey}=${encodedValues}`);
            } else if (value !== null && typeof value === "object") {
                str.push(paramsEncode(value, finalKey));
            } else if (value !== undefined) {
                str.push(`${encodeURIComponent(finalKey)}=${encodeURIComponent(value)}`);
            }
        }
    }
    return str.join("&");
}

const queryParams = {
    where: {
        name: {type: "like", value: "小"},
        state: {type: "=", value: false},
        age: {type: "in", value: "12┆13┆14"},
        time: {type: "between", value: "17:00┆17:30"}
    },
    order: {
        name: "desc",
        age: "asc"
    },
    page: {
        page: 1,
        size: 10
    }
}

const sysUserAll = {
    "fields": ["name", "nickName"]
}

const params = paramsEncode(queryParams)
console.log("?" + params);

``